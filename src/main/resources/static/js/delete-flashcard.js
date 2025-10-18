document.addEventListener('DOMContentLoaded', function() {
    const deleteButtons = document.querySelectorAll('.delete-btn');

    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {

            e.preventDefault();  // ← Add this
            e.stopPropagation(); // ← And this (prevents event bubbling)

            const cardId = this.getAttribute('data-card-id');
            console.log('Card ID:', cardId);

            const deckIdInput = document.querySelector('input[name="id"]');
            console.log('Deck input element:', deckIdInput);
            console.log('Deck ID:', deckIdInput ? deckIdInput.value : 'NOT FOUND');

            // Don't proceed if deck ID not found
            if (!deckIdInput || !deckIdInput.value) {
                alert('Error: Could not find deck ID');
                return;
            }

            const deckId = deckIdInput.value;

            if (!confirm('Are you sure you want to delete this flashcards?')) {
                return;
            }

            console.log('Sending DELETE to:', `/decks/${deckId}/flashcards/${cardId}`);

            fetch(`/decks/${deckId}/flashcards/${cardId}`, {
                method: 'DELETE'
            })
                .then(response => {
                    console.log('Response status:', response.status);
                    if (response.ok || response.status === 204) {
                        const flashcardDiv = this.closest('.flashcards-create').parentElement;
                        flashcardDiv.remove();
                        alert('Flashcard deleted successfully!');
                    } else {
                        return response.text().then(text => {
                            console.log('Error response:', text);
                            alert('Error: ' + text);
                        });
                    }
                })
                .catch(error => {
                    console.log('Fetch error:', error);
                    alert('Failed to delete flashcards: ' + error);
                });
        });
    });
});