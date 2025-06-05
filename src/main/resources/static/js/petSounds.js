/**
 * PetPal Sound System
 * Controlează redarea sunetelor pentru diferite acțiuni ale animalelor
 */
document.addEventListener('DOMContentLoaded', function() {
    console.log('Inițializare sistem de sunet PetPal');

    // Obține butoanele de acțiune
    const feedButton = document.querySelector('form[action="/feed"] button');
    const playButton = document.querySelector('form[action="/play"] button');
    const vetButton = document.querySelector('form[action="/vet"] button');

    // Obține specia animalului din atributul data-species
    const petSpecies = document.querySelector('.pet-status-card').getAttribute('data-species') || 'dog';
    console.log('Specie animal detectată:', petSpecies);

    // Adaugă event listeners pentru butoane
    if (feedButton) {
        feedButton.addEventListener('click', function() {
            playPetSound('eating', petSpecies);
        });
    }

    if (playButton) {
        playButton.addEventListener('click', function() {
            playPetSound('playing', petSpecies);
        });
    }

    if (vetButton) {
        vetButton.addEventListener('click', function() {
            playPetSound('crying', petSpecies);
        });
    }

    // Verifică dacă există un parametru pentru redarea unui sunet
    const urlParams = new URLSearchParams(window.location.search);
    const soundParam = urlParams.get('sound');
    if (soundParam) {
        playPetSound(soundParam, petSpecies);
    }
});

/**
 * Redă un sunet pentru o acțiune specifică și o specie de animal
 * @param {string} action - Acțiunea: 'eating', 'playing' sau 'crying'
 * @param {string} species - Specia animalului: 'dog', 'cat', 'bunny'
 */
function playPetSound(action, species) {
    // Construiește calea către fișierul audio
    const soundFile = `/${action}Sounds/${action}${species.charAt(0).toUpperCase() + species.slice(1)}.wav`;
    console.log('Redare sunet:', soundFile);

    // Creează și redă elementul audio
    const audio = new Audio(soundFile);

    // Adaugă gestionarea erorilor
    audio.onerror = function() {
        console.error('Eroare la încărcarea sunetului:', soundFile);
    };

    // Redă sunetul
    audio.play().catch(error => {
        console.error('Eroare la redarea sunetului:', error);
    });
}