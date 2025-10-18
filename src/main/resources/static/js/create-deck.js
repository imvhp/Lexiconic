const deckLabel = document.getElementById('deckLabel');
const flashcardContainer = document.querySelector('.flashcards-container');
const addFlashcardBtn = document.getElementById('addFlashcardBtn');

// Update the label as the deck name changes
const deckInput = document.querySelector('input[name="name"]');
if (deckInput) {
    deckInput.addEventListener('input', () => {
        deckLabel.textContent = deckInput.value || 'My Deck';
    });
}

// Helper to dynamically add flashcards
function addFlashcard() {
    // Count existing flashcards (including Thymeleaf-generated ones)
    const flashcardIndex = flashcardContainer.querySelectorAll('.flashcards-create').length;

    const flashcardHtml = `
        <div class="flashcard-create">
            <div class="form-grid">
                <div class="column front">
                    <label>Word</label>
                    <input type="text" name="flashCards[${flashcardIndex}].word" placeholder="Enter word" required>

                    <label>Pronunciation</label>
                    <input type="text" name="flashCards[${flashcardIndex}].pronunciation" placeholder="Enter pronunciation">

                    <label>Part of Speech</label>
                    <input type="text" name="flashCards[${flashcardIndex}].partOfSpeech" placeholder="e.g., noun">

                    <label>Audio URL</label>
                    <input type="text" name="flashCards[${flashcardIndex}].audio" placeholder="Paste audio link">
                </div>

                <div class="column back">
                    <label>Definition</label>
                    <textarea name="flashCards[${flashcardIndex}].definition" placeholder="Enter definition"></textarea>

                    <label>Example</label>
                    <textarea name="flashCards[${flashcardIndex}].example" placeholder="Enter example sentence"></textarea>

                    <label>Image URL</label>
                    <input type="text" name="flashCards[${flashcardIndex}].image" placeholder="Paste image link">
                </div>
            </div>
        </div>
    `;

    // Insert the new flashcards
    flashcardContainer.insertAdjacentHTML('beforeend', flashcardHtml);
}

// Initialize with 2 default flashcards if container is empty
document.addEventListener('DOMContentLoaded', () => {
    const existingCards = flashcardContainer.querySelectorAll('.flashcards-create').length;

    // Only add default cards if no cards exist (create mode)
    if (existingCards === 0) {
        addFlashcard();
        addFlashcard();
    }
});