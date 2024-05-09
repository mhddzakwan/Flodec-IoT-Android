<?php 
    include "config.php";

//$tanggal = $_POST['tanggal'];
$inputJSON = file_get_contents('php://input');
$data = json_decode($inputJSON, true);
$tanggal = $data['tanggal'];

function compareByJam($a, $b) {
    return $a['jam'] - $b['jam'];
}
    //DATA KETINGGIAN
    $rowKetinggian = array();
    $queryKetinggian = "SELECT ketinggian_air,jam FROM tb_ketinggian WHERE tanggal='$tanggal'";
    $resultKetinggian = $koneksi->query($queryKetinggian);
    if (mysqli_num_rows($resultKetinggian) > 0){
        $dataArray = array();

        // Fetch setiap baris dan masukkan ke dalam array
        while ($rowKetinggian = $resultKetinggian->fetch_assoc()) {
            $rowKetinggian['jam'] = intval($rowKetinggian['jam']);           
            $rowKetinggian['ketinggian_air'] = intval($rowKetinggian['ketinggian_air']);           
            $dataArray[] = $rowKetinggian;
        }
        usort($dataArray, 'compareByJam');
        $response['dataKetinggian'] = $dataArray;
    }

    //DATA Curah
    $rowCurah = array();
    $queryCurah = "SELECT curah_jam,jam FROM tb_curahjam WHERE tanggal='$tanggal'";
    $resultCurah = $koneksi->query($queryCurah);
    if (mysqli_num_rows($resultCurah) > 0){
        $dataCurah = array();

        // Fetch setiap baris dan masukkan ke dalam array
        while ($rowCurah = $resultCurah->fetch_assoc()) {
            $rowCurah['jam'] = intval($rowCurah['jam']);           
            $rowCurah['curah_jam'] = intval($rowCurah['curah_jam']);           
            $dataCurah[] = $rowCurah;
        }
        
        usort($dataCurah, 'compareByJam');
        $response['dataCurah'] = $dataCurah;
    }

    echo json_encode($response);
?>