# SmartHome

SmartHome là một dự án quản lý và điều khiển thiết bị nhà thông minh. Dự án này giúp người dùng kiểm soát các thiết bị điện tử trong nhà (đèn, quạt, cảm biến, v.v.) thông qua một giao diện trực quan trên thiết bị di động Android, hỗ trợ tự động hóa và lập lịch hoạt động.

## Tính năng chính

- **Điều khiển từ xa**: Bật/tắt thiết bị, điều chỉnh trạng thái qua ứng dụng Android.
- **Tự động hóa**: Thiết lập ngữ cảnh (scenario), lập lịch, tự động hóa dựa trên cảm biến hoặc thời gian. (Đang trong quá trình phát triển)
- **Theo dõi trạng thái**: Xem trạng thái thiết bị theo thời gian thực.
- **Giao diện trực quan**: Ứng dụng Android với giao diện dễ sử dụng.
- **Tích hợp IoT**: Kết nối các thiết bị IoT phổ biến thông qua Firebase.

## Công nghệ sử dụng

- **Ngôn ngữ lập trình:** Kotlin
- **IDE:** Android Studio
- **Backend & Database:** Firebase (Realtime Database/Firestore, Authentication, Cloud Messaging, v.v.)
- **Realtime Communication:** Firebase Realtime Database/Firestore
- **MTQ/HTTP:** Giao tiếp với thiết bị IoT

## Demo ứng dụng Youtube: [https://youtu.be/6QbY2vy2Cu0](https://www.youtube.com/watch?v=6QbY2vy2Cu0)


## Hướng dẫn cài đặt

1. **Clone dự án:**
   ```bash
   git clone https://github.com/Bin6789/SmartHome.git
   ```
2. **Mở dự án bằng Android Studio:**
   - Chọn `Open an existing project` và trỏ đến thư mục vừa clone.
3. **Cài đặt dependencies:**
   - Đảm bảo đã cài đặt tất cả dependencies trong file `build.gradle` (Firebase, AndroidX, Material, v.v.).
   - Android Studio sẽ tự động đề xuất cài đặt các dependencies còn thiếu.
4. **Cấu hình Firebase:**
   - Truy cập [Firebase Console](https://console.firebase.google.com/), tạo project mới (nếu chưa có).
   - Thêm ứng dụng Android và tải file `google-services.json` về.
   - Đặt file `google-services.json` vào thư mục `app/` của dự án.
5. **Chạy ứng dụng:**
   - Kết nối thiết bị Android hoặc sử dụng Android Emulator.
   - Nhấn Run (Shift + F10) để build và chạy ứng dụng.

## Đóng góp

Rất hoan nghênh các đóng góp từ cộng đồng!

- Fork dự án, tạo nhánh mới và gửi pull request.
- Báo lỗi hoặc đề xuất tính năng mới qua [Issues](https://github.com/Bin6789/SmartHome/issues).

## License

Dự án được phát hành theo giấy phép MIT. Xem chi tiết tại file [LICENSE](LICENSE).

---

> **Liên hệ:**  
> - Email: nhuongblue.email@example.com  
> 
> © 2025 Bin6789
