document.addEventListener('DOMContentLoaded', e => {
    const _document = e.target;
    _document.addEventListener('input', e => {
        handleDateInput(e);
    });
    _document.addEventListener('click', e => {
        handleDetails(e);
    });

     /**
     * Handles input events on date, time, and datetime-local input elements.
     * Updates the minimum and maximum values of referenced inputs based on the current value.
     * Reports validity issues if the input is invalid.
     *
     * @param {InputEvent} e - The input event triggered on the document.
     */
    const handleDateInput = e => {
        const types = ['datetime-local', 'date', 'time'];
        if (!types.includes(e.target.type)) {
            return;
        }

        const {minRef, maxRef} = e.target.dataset;
        const $start = minRef && document.getElementById(minRef);
        if ($start) {
            $start.max = e.target.value;
        }
        const $end = maxRef && document.getElementById(maxRef);
        if ($end) {
            $end.min = e.target.value;
        }

        if (!e.target.validity.valid) {
            e.target.reportValidity();
        }
    };

    /**
     * Handles click events on expand and collapse buttons.
     * Expands or collapses all <details> elements inside a specified target element.
     *
     * @param {MouseEvent} e - The click event triggered on the document.
     */
    const handleDetails = e => {
        const $expandButton = e.target.closest(`button[name="expand"]`);
        if ($expandButton) {
            const $target = document.getElementById($expandButton.dataset.targetRef);
            const $details = $target.querySelectorAll('details');
            [...$details].forEach(detail => detail.open = true);
        }

        const $collapseButton = e.target.closest(`button[name="collapse"]`);
        if ($collapseButton) {
            const $target = document.getElementById($collapseButton.dataset.targetRef);
            const $details = $target.querySelectorAll('details');
            [...$details].forEach(detail => detail.open = false);
        }
    };
});
