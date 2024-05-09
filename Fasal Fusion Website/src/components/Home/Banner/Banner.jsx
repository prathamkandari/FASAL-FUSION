import "./Banner.scss";
const Banner = () => {
    const hyperlink = () => {
        window.location.href = "https://fasal-fusion-new.vercel.app/";
    };
    const hyperlinktwo = () => {
        window.location.href = "./realtimegraphs";
    };
    return (<div className="hero-banner"><div className="content">
        <div className="text-content">
        <h1 style={{fontSize: '80px'}}>FASAL FUSION</h1>
        <p>An Algorithmic Approach to Transform Crop Recommendations
        </p>
        <div className="ctas">
            <button className="banner-cta" onClick={hyperlink}>Predict your Crop</button>
            <button className="banner-cta v2" onClick={hyperlinktwo}>View Device</button>
        </div>
        </div>
        </div>
        </div>);
};
export default Banner;
