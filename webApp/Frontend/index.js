function changeLanguage() {
    var labels = document.querySelectorAll('.row label');
    var isHindi = document.getElementById('language-button').getAttribute('data-language') === 'hindi';

    // Define translations
    var translations = {
        'english': ['Nitrogen', 'Phosphorus', 'Potassium', 'Temperature', 'pH', 'Humidity', 'Rainfall'],
        'hindi': ['नाइट्रोजन', 'फास्फोरस', 'पोटैशियम', 'तापमान', 'पीएच मान', 'आर्द्रता', 'वर्षा']
    };

    // Set the label texts based on the current language
    labels.forEach((label, index) => {
        label.textContent = isHindi ? translations['hindi'][index] : translations['english'][index];
    });

    // Toggle the language attribute
    document.getElementById('language-button').setAttribute('data-language', isHindi ? 'english' : 'hindi');
}
document.getElementById('apiButton').addEventListener('click', function() {

// Sample input data
const inputData = {
    N: 100.0,
    P: 50.0,
    K: 30.0,
    temp: 25.0,
    humidity: 70.0,
    ph: 6.5,
    rain: 50.0
  };
  
  // AWS API Gateway endpoint
  const apiEndpoint = 'https://9jecftzm79.execute-api.us-east-1.amazonaws.com/phase2/sum';
  
  // Fetch options
  const fetchOptions = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
      // Add any additional headers if needed
    },
    body: JSON.stringify(inputData)
  };
  
  // Make the API call
  fetch(apiEndpoint, fetchOptions)
    .then(response => {
      // Check if the response status is OK (2xx)
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      // Parse the JSON response
      return response.json();
    })
    .then(result => {
      // Process the result
      console.log('API Result:', result);
    })
    .catch(error => {
      // Handle errors
      console.error('Error:', error.message);
    });
  
}); 