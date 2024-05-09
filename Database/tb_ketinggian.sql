-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 21 Apr 2024 pada 11.07
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
-- Struktur dari tabel `tb_ketinggian`
--

CREATE TABLE `tb_ketinggian` (
  `id` int(11) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `tanggal` varchar(100) NOT NULL,
  `jam` int(5) NOT NULL,
  `ketinggian_air` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_ketinggian`
--

INSERT INTO `tb_ketinggian` (`id`, `latitude`, `longitude`, `tanggal`, `jam`, `ketinggian_air`) VALUES
(38, 3.60527, 98.6261, '04-04-2024', 1, 0),
(39, 3.60527, 98.6261, '04-04-2024', 2, 5),
(40, 3.60527, 98.6261, '04-04-2024', 3, 10),
(41, 3.60527, 98.6261, '04-04-2024', 4, 20),
(42, 3.60527, 98.6261, '04-04-2024', 5, 30),
(43, 3.60527, 98.6261, '04-04-2024', 6, 25),
(44, 3.60527, 98.6261, '04-04-2024', 7, 40),
(45, 3.60527, 98.6261, '04-04-2024', 8, 30);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `tb_ketinggian`
--
ALTER TABLE `tb_ketinggian`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `tb_ketinggian`
--
ALTER TABLE `tb_ketinggian`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
