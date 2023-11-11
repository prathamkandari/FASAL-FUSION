function changeLanguage() {
    // This function is just a placeholder.
    // You will need to implement actual translation logic depending on your setup.
  
    // Example of toggling language text
    var labels = document.querySelectorAll('.form-group label');
    var isHindi = document.getElementById('language-button').getAttribute('data-language') === 'hindi';
  
    // Define translations (example only - normally you would fetch these or use a library)
    var translations = {
      'english': ['Nitrogen', 'Phosphorous', 'Potassium', 'Rainfall', 'pH Values', 'Humidity', 'Temperatures'],
      'hindi': ['नाइट्रोजन', 'फास्फोरस', 'पोटैशियम', 'वर्षा', 'पीएच मान', 'आर्द्रता', 'तापमान']
    };
  
    // Set the label texts based on the current language
    labels.forEach((label, index) => {
      label.textContent = isHindi ? translations['hindi'][index] : translations['english'][index];
    });
  
    // Toggle the language attribute
    document.getElementById('language-button').setAttribute('data-language', isHindi ? 'english' : 'hindi');
  }
  