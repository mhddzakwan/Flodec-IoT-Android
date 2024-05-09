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
-- Struktur dari tabel `tb_ketinggianmenit`
--

CREATE TABLE `tb_ketinggianmenit` (
  `id` int(5) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `tanggal` varchar(20) NOT NULL,
  `jam` int(5) NOT NULL,
  `menit` int(5) NOT NULL,
  `ketinggian_air` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_ketinggianmenit`
--

INSERT INTO `tb_ketinggianmenit` (`id`, `latitude`, `longitude`, `tanggal`, `jam`, `menit`, `ketinggian_air`) VALUES
(18, 3.60458, 98.6248, '04-04-2023', 7, 1, 20),
(154, 3.60458, 98.6248, '04-04-2023', 5, 1, 10),
(155, 3.60458, 98.6248, '04-04-2023', 5, 2, 12),
(156, 3.60458, 98.6248, '04-04-2023', 5, 3, 14),
(166, 3.60458, 98.6248, '04-04-2023', 5, 4, 14),
(167, 3.6, 98.62, '21-04-24', 9, 4, 10),
(168, 3.6, 98.62, '21-04-24', 9, 5, 12),
(169, 3.6, 98.62, '21-04-24', 9, 7, 14),
(170, 3.6, 98.62, '21-04-24', 9, 8, 16),
(171, 3.6, 98.62, '21-04-24', 9, 9, 18),
(172, 3.6, 98.62, '21-04-24', 9, 10, 20),
(173, 3.6, 98.62, '21-04-24', 9, 11, 20);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `tb_ketinggianmenit`
--
ALTER TABLE `tb_ketinggianmenit`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `tb_ketinggianmenit`
--
ALTER TABLE `tb_ketinggianmenit`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=174;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
