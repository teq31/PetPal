document.addEventListener('DOMContentLoaded', function() {
    console.log('Inițializare sistem de sunet PetPal');

    const feedButton = document.querySelector('form[action="/feed"] button');
    const playButton = document.querySelector('form[action="/play"] button');
    const vetButton = document.querySelector('form[action="/vet"] button');

    const petSpeciesElement = document.querySelector('.pet-status-card');
    const petSpecies = petSpeciesElement ? petSpeciesElement.getAttribute('data-species') || 'dog' : 'dog';
    console.log('Specie animal detectată:', petSpecies);

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

    const taskButtons = document.querySelectorAll('form[action="/task/complete"] button');
    taskButtons.forEach(button => {
        const taskName = button.closest('.task-item').querySelector('.task-name').textContent;
        if (taskName.includes('Feed')) {
            button.addEventListener('click', function() {
                playPetSound('eating', petSpecies);
            });
        } else if (taskName.includes('Play') || taskName.includes('Walk') || taskName.includes('Groom')) {
            button.addEventListener('click', function() {
                playPetSound('playing', petSpecies);
            });
        } else if (taskName.includes('vet')) {
            button.addEventListener('click', function() {
                playPetSound('crying', petSpecies);
            });
        }
    });

    const urlParams = new URLSearchParams(window.location.search);
    const soundParam = urlParams.get('sound');
    if (soundParam) {
        playPetSound(soundParam, petSpecies);

        // Remove the sound parameter from URL to prevent playing sound on refresh
        const newUrl = window.location.protocol + "//" + window.location.host + window.location.pathname;
        window.history.replaceState({path: newUrl}, '', newUrl);
    }
});

function playPetSound(action, species) {
    const soundFile = `/${action}Sounds/${action}${species.charAt(0).toUpperCase() + species.slice(1)}.wav`;
    console.log('Redare sunet:', soundFile);

    const audio = new Audio(soundFile);

    audio.onerror = function() {
        console.error('Eroare la încărcarea sunetului:', soundFile);
    };

    audio.play().catch(error => {
        console.error('Eroare la redarea sunetului:', error);
    });
}