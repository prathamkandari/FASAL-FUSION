import React, { useEffect, useState } from 'react';
import './Home.css';
import Banner from './Banner/Banner';
import { Link } from 'react-router-dom';
import { Modal } from "react-responsive-modal";
import { FaLinkedin } from "react-icons/fa";
import 'react-responsive-modal/styles.css';
import 'firebase/firestore';
import { db } from '../../firebase-config/config';

const Plants = ({ name, image, onOpenModal }) => {
    return (
        <div className="grocery-card" onClick={onOpenModal}>
            <div className="image-container">
                <img src={image} alt={name} />
                <div className="name-overlay">
                    <p>{name}</p>
                </div>
            </div>
        </div>
    );
};

const Home = ({ theme }) => {
    const [plants, setPlants] = useState([]);
    const [open, setOpen] = useState(false);
    const [selectedPlant, setSelectedPlant] = useState(null);

    const onOpenModal = (plant) => {
        setSelectedPlant(plant);
        setOpen(true);
    };

    const onCloseModal = () => {
        setOpen(false);
    };

    useEffect(() => {
        // Function to fetch plant data from Firestore
        const fetchPlantsData = async () => {
            try {
                const plantData = [];
                const querySnapshot = await db.collection("plant_data").get();
                querySnapshot.forEach((doc) => {
                    const data = doc.data();
                    plantData.push({
                        name: data.Name,
                        image: data.plant_image,
                        scientificName: data['Scientific Name'][0],
                        waterIntake: data['Water Intake'][0],
                        humidity: data['Humidity'][0],
                        lightIntake: data['Light Intake']['preferred_light'],
                        soilCondition: data['Soil Condition'][0],
                        temperature: data['Temperature']['Seasons']['Summer'].join(' to ') + ' (Summer), ' + data['Temperature']['Seasons']['Winter'].join(' to ') + ' (Winter)',
                        commonProblems: data['Common Problems'].join(', ')
                    });
                });
                setPlants(plantData);
            } catch (error) {
                console.error("Error fetching plant data:", error);
            }
        };

        // Call the function to fetch plant data when the component mounts
        fetchPlantsData();
    }, []);

    return (
        <>
            <Banner />
            <h1 style={{ color: '#67C440', paddingTop: '20px', textAlign: 'center' }}>Product Catalog</h1>
            <div className="groceries-list">
                <div className="category-container">
                    {plants.map((plant, index) => (
                        <div key={index} className="grocery-card" onClick={() => onOpenModal(plant)}>
                            <div className="image-container">
                                <img src={plant.image} alt={plant.name} />
                                <div className="name-overlay">
                                    <p>{plant.name}</p>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
            <Modal
                open={open}
                onClose={onCloseModal}
                center
                classNames={{
                    overlay: "customOverlay2",
                    modal: "customModal2",
                }}
            >
                <div className="container-fluid team-modal backdrop-saturate-125 justify-content-center gap-5 text-white">
                    {selectedPlant && (
                        <div>
                            <h2>{selectedPlant.name}</h2>
                            <p><strong>Scientific Name:</strong> {selectedPlant.scientificName}</p>
                            <p><strong>Water Intake:</strong> {selectedPlant.waterIntake}</p>
                            <p><strong>Humidity:</strong> {selectedPlant.humidity}</p>
                            <p><strong>Light Intake:</strong> {selectedPlant.lightIntake}</p>
                            <p><strong>Soil Condition:</strong> {selectedPlant.soilCondition}</p>
                            <p><strong>Temperature:</strong> {selectedPlant.temperature}</p>
                            <p><strong>Common Problems:</strong> {selectedPlant.commonProblems}</p>
                        </div>
                    )}
                </div>
            </Modal>
        </>
    );
};

export default Home;
