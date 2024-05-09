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
-- Struktur dari tabel `tb_curahjam`
--

CREATE TABLE `tb_curahjam` (
  `id` int(5) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `tanggal` varchar(15) NOT NULL,
  `jam` int(5) NOT NULL,
  `curah_jam` int(10) NOT NULL,
  `cuaca` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_curahjam`
--

INSERT INTO `tb_curahjam` (`id`, `latitude`, `longitude`, `tanggal`, `jam`, `curah_jam`, `cuaca`) VALUES
(38, 3.60527, 98.6261, '04-04-2024', 1, 0, 'Cerah'),
(39, 3.60527, 98.6261, '04-04-2024', 2, 1, 'Berawan'),
(40, 3.60527, 98.6261, '04-04-2024', 3, 1, 'Berawan'),
(41, 3.60527, 98.6261, '04-04-2024', 4, 2, 'Berawan'),
(42, 3.60527, 98.6261, '04-04-2024', 5, 2, 'Berawan'),
(43, 3.60527, 98.6261, '04-04-2024', 6, 3, 'Hujan Ringan'),
(44, 3.60527, 98.6261, '04-04-2024', 7, 4, 'Hujan Ringan'),
(45, 3.60527, 0, '04-04-2024', 8, 6, 'Hujan Ringan'),
(46, 3.60527, 98.6261, '04-04-2024', 9, 7, 'Hujan Ringan');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `tb_curahjam`
--
ALTER TABLE `tb_curahjam`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `tb_curahjam`
--
ALTER TABLE `tb_curahjam`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
