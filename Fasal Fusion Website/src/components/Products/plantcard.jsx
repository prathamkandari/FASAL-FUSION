// PlantCard.jsx
import "./plantcard.scss";

const PlantCard = ({ data }) => {
    return (
        <div className="plant-card">
            <div className="image-container">
                <img src={data.image} alt={data.name} />
            </div>
            <div className="details">
                <div className="scientific-name">{data["Scientific Name"][0]}</div>
                <div className="humidity">{data.Humidity[0]}</div>
                <div className="water-intake">{data["Water Intake"][0]}</div>
                <div className="common-problems">
                    <ul>
                        {data["Common Problems"].map((problem, index) => (
                            <li key={index}>{problem}</li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default PlantCard;
