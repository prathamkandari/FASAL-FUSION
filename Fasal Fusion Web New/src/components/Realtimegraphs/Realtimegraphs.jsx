import React, { useEffect, useState } from 'react';
import firebase from 'firebase/compat/app';
import 'firebase/compat/database';
import { TableContainer, Table, TableHead, TableBody, TableRow, TableCell, Paper, TablePagination } from '@mui/material';
import Chart from 'chart.js/auto'; // Import Chart.js

const Realtimegraphs = () => {
    const [data, setData] = useState([]);
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);

    useEffect(() => {
        const database = firebase.database();
        const dataRef = database.ref('UsersData/oZhKlnqNQ6NuOfYSbaThe3aD32A2/readings');

        dataRef.once('value')
            .then(snapshot => {
                if (snapshot.exists()) {
                    const data = snapshot.val();
                    // Convert data object to array and sort in descending order by timestamp
                    const dataArray = Object.values(data).sort((a, b) => b.timestamp - a.timestamp);
                    setData(dataArray);

                    // Extract top 10 values for each parameter
                    const top10Data = dataArray.slice(0, 10);

                    // Create Humidity graph
                    const humidityLabels = top10Data.map(item => item.timestamp);
                    const humidityData = top10Data.map(item => item.humidity);
                    createGraph('humidityChart', humidityLabels, humidityData, 'Humidity');

                    // Create Moisture graph
                    const moistureData = top10Data.map(item => item.moisture);
                    createGraph('moistureChart', humidityLabels, moistureData, 'Moisture');

                    // Create Temperature graph
                    const temperatureData = top10Data.map(item => item.temperature);
                    createGraph('temperatureChart', humidityLabels, temperatureData, 'Temperature');
                } else {
                    console.log('No data available');
                }
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });

        // Clean up function
        return () => {
            // Unsubscribe from Firebase Realtime Database
            dataRef.off();
        };
    }, []);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const createGraph = (canvasId, labels, data, label) => {
        const ctx = document.getElementById(canvasId).getContext('2d');
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: label,
                    data: data,
                    fill: false,
                    borderColor: 'rgb(75, 192, 192)',
                    tension: 0.1
                }]
            },
        });
    };

    return (
        <div>
            <h1 style={{ color: '#67C440', paddingTop: '20px', paddingBottom: '20px', textAlign: 'center' }}>Device Details</h1>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Humidity</TableCell>
                            <TableCell>Moisture</TableCell>
                            <TableCell>Temperature</TableCell>
                            <TableCell>Timestamp</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {(rowsPerPage > 0
                            ? data.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                            : data
                        ).map((item, index) => (
                            <TableRow key={index}>
                                <TableCell>{item.humidity}</TableCell>
                                <TableCell>{item.moisture}</TableCell>
                                <TableCell>{item.temperature}</TableCell>
                                <TableCell>{item.timestamp}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <TablePagination
                rowsPerPageOptions={[5, 10, 25, { label: 'All', value: -1 }]}
                component="div"
                count={data.length}
                rowsPerPage={rowsPerPage}
                page={page}
                onPageChange={handleChangePage}
                onRowsPerPageChange={handleChangeRowsPerPage}
            />

            <h1 style={{ color: '#67C440', paddingTop: '20px', paddingBottom: '20px', textAlign: 'center' }}>Graphical Representation</h1>

            {/* Humidity Graph */}
            <canvas id="humidityChart" style={{ height: '400px', width: '100%', marginBottom: '20px' }} />

            {/* Moisture Graph */}
            <canvas id="moistureChart" style={{ height: '400px', width: '100%', marginBottom: '20px' }} />

            {/* Temperature Graph */}
            <canvas id="temperatureChart" style={{ height: '400px', width: '100%', marginBottom: '20px' }} />
        </div>
    );
}

export default Realtimegraphs;
