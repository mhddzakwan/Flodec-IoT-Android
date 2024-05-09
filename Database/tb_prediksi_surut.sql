-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 21 Apr 2024 pada 11.08
-- Versi server: 10.4.27-MariaDB
-- Versi PHP: 8.0.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_aquawatch`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_prediksi_surut`
--

CREATE TABLE `tb_prediksi_surut` (
  `id` int(5) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `rata2_curah` float NOT NULL,
  `prediksi_menit` int(10) NOT NULL,
  `prediksi_berikutnya` varchar(25) NOT NULL,
  `debit` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_prediksi_surut`
--

INSERT INTO `tb_prediksi_surut` (`id`, `latitude`, `longitude`, `rata2_curah`, `prediksi_menit`, `prediksi_berikutnya`, `debit`) VALUES
(1, 3.60527, 98.6261, 0, 0, '0', 0);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `tb_prediksi_surut`
--
ALTER TABLE `tb_prediksi_surut`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `tb_prediksi_surut`
--
ALTER TABLE `tb_prediksi_surut`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
