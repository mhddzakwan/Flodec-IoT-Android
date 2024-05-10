-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 21 Apr 2024 pada 11.06
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
-- Struktur dari tabel `tb_curah`
--

CREATE TABLE `tb_curah` (
  `id` int(5) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `tanggal` varchar(20) NOT NULL,
  `jam` int(5) NOT NULL,
  `menit` int(5) NOT NULL,
  `curah_menit` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_curah`
--

INSERT INTO `tb_curah` (`id`, `latitude`, `longitude`, `tanggal`, `jam`, `menit`, `curah_menit`) VALUES
(1, 3.60527, 98.6261, '04-04-2024', 8, 10, 5),
(5, 3.60458, 98.6248, '04-04-2023', 5, 10, 15),
(167, 3.60458, 98.6248, '04-04-2023', 5, 1, 10),
(168, 3.60458, 98.6248, '04-04-2023', 5, 2, 12),
(169, 3.60458, 98.6248, '04-04-2023', 5, 3, 14),
(179, 3.60458, 98.6248, '04-04-2023', 5, 4, 14),
(180, 3.6, 98.62, '21-04-24', 9, 4, 2.82),
(181, 3.6, 98.62, '21-04-24', 9, 5, 1.88),
(182, 3.6, 98.62, '21-04-24', 9, 7, 0),
(183, 3.6, 98.62, '21-04-24', 9, 8, 16),
(184, 3.6, 98.62, '21-04-24', 9, 9, 18),
(185, 3.6, 98.62, '21-04-24', 9, 10, 20),
(186, 3.6, 98.62, '21-04-24', 9, 11, 20);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `tb_curah`
--
ALTER TABLE `tb_curah`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `tb_curah`
--
ALTER TABLE `tb_curah`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=187;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
