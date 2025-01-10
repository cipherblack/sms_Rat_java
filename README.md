# راهنمای استفاده از پروژه جاوا

این فایل راهنمایی است برای راه‌اندازی و اجرای پروژه جاوا شما. لطفاً مراحل زیر را به دقت دنبال کنید:

## پیش‌نیازها
- اطمینان حاصل کنید که **JDK 11 یا نسخه‌های جدیدتر** بر روی سیستم شما نصب شده باشد.
- یک IDE مانند **IntelliJ IDEA** یا **Eclipse** نصب کنید.

## مراحل راه‌اندازی
1. **دانلود و استخراج پروژه**:
   - فایل زیپ پروژه را دانلود کرده و در مسیر دلخواه خود استخراج کنید.

2. **ایمپورت پروژه به IDE**:
   - IDE خود را باز کنید.
   - از منوی اصلی، گزینه `File > Open` را انتخاب کرده و پوشه پروژه را انتخاب کنید.
   - مطمئن شوید که پروژه به‌عنوان یک پروژه Maven یا Gradle شناسایی شده است (در صورت نیاز `pom.xml` یا `build.gradle` را وارد کنید).

3. **پیکربندی متغیرها و تنظیمات**:
   - فایل‌های تنظیمات را باز کنید (در صورت وجود، مانند `application.properties` یا `config.json`).
   - مقادیر مورد نیاز را با توجه به محیط خود (مانند آدرس‌های سرور یا کلیدهای API) تغییر دهید.

4. **بیلد پروژه**:
   - اگر از Maven استفاده می‌کنید:
     ```bash
     mvn clean install
     ```
   - اگر از Gradle استفاده می‌کنید:
     ```bash
     gradle build
     ```

5. **اجرای پروژه**:
   - کلاس اصلی پروژه را پیدا کنید (معمولاً کلاسی که شامل متد `public static void main` است).
   - بر روی کلاس راست‌کلیک کرده و گزینه `Run` را انتخاب کنید.

## مشکلات احتمالی
- اگر با خطای مربوط به وابستگی‌ها مواجه شدید، اطمینان حاصل کنید که به اینترنت متصل هستید تا وابستگی‌ها دانلود شوند.
- در صورت وجود خطاهای مرتبط با نسخه JDK، نسخه JDK تنظیم شده در IDE را بررسی کنید.

## پشتیبانی
اگر به مشکلی برخوردید یا سوالی داشتید، لطفاً با مدیر پروژه تماس بگیرید.

