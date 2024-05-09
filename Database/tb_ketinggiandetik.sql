-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 01 Bulan Mei 2024 pada 12.21
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
-- Struktur dari tabel `tb_ketinggiandetik`
--

CREATE TABLE `tb_ketinggiandetik` (
  `id` int(10) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `tanggal` varchar(20) NOT NULL,
  `jam` int(5) NOT NULL,
  `menit` int(5) NOT NULL,
  `detik` int(5) NOT NULL,
  `ketinggian_air` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_ketinggiandetik`
--

INSERT INTO `tb_ketinggiandetik` (`id`, `latitude`, `longitude`, `tanggal`, `jam`, `menit`, `detik`, `ketinggian_air`) VALUES
(1, 3.60527, 98.6261, '04-04-2024', 5, 10, 0, 10),
(3, 3.60527, 98.6261, '04-04-2024', 5, 10, 20, 10),
(5, 3.60527, 98.6261, '04-04-2024', 5, 10, 40, 17),
(7, 3.60527, 98.6261, '04-04-2024', 5, 11, 0, 16),
(12, 3.60458, 98.6248, '04-05-2023', 7, 30, 0, 25);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `tb_ketinggiandetik`
--
ALTER TABLE `tb_ketinggiandetik`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `tb_ketinggiandetik`
--
ALTER TABLE `tb_ketinggiandetik`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
