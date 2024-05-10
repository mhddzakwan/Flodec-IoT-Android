# Flodec

FLODEC adalah projek sistem peringatan banjir sedini mungkin yang akan dapat diakses oleh pengguna.


FLODEC menampilkan wilayah yang rawan banjir, data prediksi naik dan surut, serta alternatif jalan lain yang dapat dilewati pengguna ketika terdapat jalan yang tidak dapat diakses.

Flodec secara tepat menyasar dua poin SDGs yaitu poin 9 dan 11.

## Flodec's Techstack

FLODEC terdiri atas dua perangkat, pertama yaitu perangkat Internet of Things (IoT) dan kedua yaitu aplikasi _mobile_.

IoT diimplementasikan menggunakan NodeMCU dan ESP32 CAM sebagai mikrokomtroller. project IoT ini menggunakan bahasa C/C++ dan beberapa perangkat keras, seperti sensor jarak (HCSR04), _rain gauge_, kamera (ESP32-CAM), dan beberapa komponen penting lainnya.  

Aplikasi _mobile_ dibuat dengan Android Studio dengan bahasa pemrograman Kotlin.

Sisi server/ _backend_ dibuat dengan _Native_ PHP dan penggunaan Firebase untuk pengiriman notifikasi push

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)
