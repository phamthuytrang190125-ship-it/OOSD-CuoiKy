# Hệ thống quản lý sự cố môi trường

Đồ án cuối kỳ môn Phương pháp phát triển phần mềm hướng đối tượng (OOSD), lớp CNPM2.

Hệ thống hỗ trợ người dân gửi báo cáo sự cố môi trường và tra cứu tiến độ xử lý. Nhân viên nội bộ có thể tiếp nhận hồ sơ, phân công tổ đội và cập nhật kết quả xử lý.

## Chức năng chính

| Vai trò | Chức năng |
| --- | --- |
| Người dân | Gửi báo cáo kèm hình ảnh, tra cứu trạng thái và xem danh sách sự cố công khai. |
| Điều phối viên | Xem hồ sơ, duyệt hoặc từ chối báo cáo, lập phiếu phân công cho tổ đội. |
| Nhân viên hiện trường | Xem nhiệm vụ được giao cho tổ đội và cập nhật kết quả xử lý. |
| Quản trị viên | Xem các màn hình quản lý tài khoản, danh mục loại sự cố và tổ đội. |

Hệ thống có trang thống kê để tổng hợp tình hình tiếp nhận và xử lý sự cố.

## Công nghệ sử dụng

- **Backend** — Java 17, Spring Boot, Spring MVC và Spring Data JPA.
- **Giao diện** — HTML và Thymeleaf.
- **Cơ sở dữ liệu** — Oracle Database, JDBC và SQL/PLSQL.
- **Công cụ build** — Maven Wrapper.

## Cấu trúc thư mục

| Tệp hoặc thư mục | Nội dung |
| --- | --- |
| [`baocaosuco/`](baocaosuco/) | Mã nguồn ứng dụng Spring Boot, giao diện và ảnh mẫu. |
| [`QL_SUCO_MT.sql`](QL_SUCO_MT.sql) | Script tạo bảng, dữ liệu mẫu, view và trigger trong Oracle. |
| [`PhanTichThietKe/`](PhanTichThietKe/) | Sơ đồ phân tích thiết kế và hình ảnh minh họa. |
| [`TaiLieuThamKhao/`](TaiLieuThamKhao/) | Tài liệu tham khảo của đồ án. |
| `BÁO CÁO ĐỒ ÁN OOSD.docx` | Báo cáo cuối kỳ. |

## Hướng dẫn cài đặt và chạy

### 1. Chuẩn bị

- Cài JDK 17.
- Chuẩn bị Oracle Database đang chạy và một tài khoản Oracle dành cho đồ án.
- Có thể dùng Oracle SQL Developer để chạy script cơ sở dữ liệu.
- Kết nối Internet trong lần đầu chạy Maven Wrapper để tải các thư viện cần thiết.

### 2. Tải mã nguồn

```bash
git clone https://github.com/phamthuytrang190125-ship-it/OOSD-CuoiKy.git
cd OOSD-CuoiKy
```

### 3. Khởi tạo cơ sở dữ liệu

Trong Oracle SQL Developer, kết nối bằng tài khoản dành cho đồ án. Mở file `QL_SUCO_MT.sql` và chọn **Run Script (F5)** để tạo các bảng và nạp dữ liệu mẫu. Nên dùng một schema mới để tránh trùng bảng hoặc dữ liệu.

### 4. Cấu hình kết nối và khởi động ứng dụng

Trên macOS, mở Terminal và chạy:

```zsh
cd baocaosuco
export SPRING_DATASOURCE_URL='jdbc:oracle:thin:@//localhost:1521/TEN_SERVICE'
export SPRING_DATASOURCE_USERNAME='TEN_SCHEMA'
read -rs 'SPRING_DATASOURCE_PASSWORD?Mat khau Oracle: '
export SPRING_DATASOURCE_PASSWORD
bash ./mvnw spring-boot:run
```

Thay `TEN_SERVICE` bằng tên service của Oracle và `TEN_SCHEMA` bằng tên tài khoản đã dùng để chạy script SQL. Đổi `localhost` và `1521` nếu Oracle chạy trên máy hoặc cổng khác. Khi Terminal hỏi mật khẩu, nhập mật khẩu Oracle rồi nhấn Enter.

Nếu chạy bằng IDE, đặt ba biến môi trường trên trong cấu hình chạy của ứng dụng. Nếu dùng Windows PowerShell, đặt các biến môi trường tương ứng rồi chạy `.\mvnw.cmd spring-boot:run` trong thư mục `baocaosuco/`.

Sau khi ứng dụng khởi động, mở [http://localhost:8082](http://localhost:8082).

## Chạy thử luồng xử lý sự cố

1. Mở [trang gửi báo cáo](http://localhost:8082/baocao), điền thông tin sự cố và gửi hồ sơ.
2. Mở [màn hình điều phối](http://localhost:8082/admin?username=dieuphoi1), tìm hồ sơ vừa gửi, duyệt và phân công tổ đội.
3. Mở [màn hình nhiệm vụ](http://localhost:8082/nhiemvu?username=nhanvien1) và chọn tài khoản nhân viên mẫu tương ứng với tổ đội được giao bằng tham số `username`. Mở nhiệm vụ và cập nhật kết quả.
4. Quay lại [trang tra cứu](http://localhost:8082/tracuu) để xem trạng thái xử lý.

Bản demo sử dụng tham số `username` để chuyển giữa các vai trò nội bộ.
