let currentIndex = 0;
const flashcards = document.querySelectorAll('.flashcard');
const totalCards = flashcards.length;
const deckId = window.location.pathname.split('/')[2]; // Extract deckId from URL

// Play audio function
function playAudio(btn) {
    const url = btn.dataset.audio;
    if (url) {
        const audio = new Audio(url);
        audio.play();
    }
}

// Flip current card
function flipCard() {
    if (flashcards[currentIndex]) {
        flashcards[currentIndex].classList.toggle('flipped');
    }
}

// Navigate to next/previous card
function navigateCard(direction) {
    const currentCard = flashcards[currentIndex];

    // Remove flipped state before switching
    currentCard.classList.remove('flipped');
    currentCard.classList.remove('active');
    currentCard.classList.add('hidden');

    // Update index
    if (direction === 'next' && currentIndex < totalCards - 1) {
        currentIndex++;
    } else if (direction === 'prev' && currentIndex > 0) {
        currentIndex--;
    } else if (direction === 'next' && currentIndex === totalCards - 1) {
        // Reached the end
        showCompletion();
        return;
    }

    // Show new card
    const newCard = flashcards[currentIndex];
    newCard.classList.remove('hidden');
    newCard.classList.add('active');

    // Update progress counter
    document.getElementById('currentCard').textContent = currentIndex + 1;
}

// Rate card and move to next
function rateCard(quality) {
    const currentCard = flashcards[currentIndex];
    const flashCardId = currentCard.dataset.flashcardId;

    if (!flashCardId) {
        console.error('No flashcard ID found');
        navigateCard('next');
        return;
    }

    // Send rating to backend
    fetch(`/decks/${deckId}/flashcards/reviews`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            flashCardId: flashCardId,
            quality: quality  // Your backend expects 'quality' not 'rating'
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('Review saved:', data);
            navigateCard('next');
        })
        .catch(error => {
            console.error('Error saving review:', error);
            navigateCard('next'); // Move on even if save fails
        });
}

// Show completion screen
function showCompletion() {
    const reviewWrapper = document.querySelector('.review-wrapper');
    if (reviewWrapper) {
        reviewWrapper.style.display = 'none';
    }

    // Find the existing completion card and show it
    const completionCard = document.querySelector('.completion-card');
    if (completionCard) {
        // Using 'flex' will likely work best with your existing centering styles
        completionCard.style.display = 'flex';
    }
}

// Click on card to flip
document.querySelectorAll('.flashcard').forEach(card => {
    card.addEventListener('click', (e) => {
        // Don't flip if clicking audio button
        if (!e.target.classList.contains('audio-btn')) {
            flipCard();
        }
    });
});

// Keyboard controls
document.addEventListener('keydown', (e) => {
    switch(e.key) {
        case ' ':           // Spacebar to flip
        case 'Enter':       // Enter to flip
            e.preventDefault();
            flipCard();
            break;
        case 'ArrowRight':  // Right arrow for next
            e.preventDefault();
            navigateCard('next');
            break;
        case 'ArrowLeft':   // Left arrow for previous
            e.preventDefault();
            navigateCard('prev');
            break;
        case '1':           // Number keys for rating (0 = Again)
            e.preventDefault();
            rateCard(0);
            break;
        case '2':           // 1 = Hard
            e.preventDefault();
            rateCard(1);
            break;
        case '3':           // 2 = Good
            e.preventDefault();
            rateCard(2);
            break;
        case '4':           // 3 = Easy
            e.preventDefault();
            rateCard(3);
            break;
        case '5':           // 4 = Perfect
            e.preventDefault();
            rateCard(5);
            break;
    }
});

// Initialize first card
if (totalCards > 0) {
    flashcards[0].classList.add('active');
}