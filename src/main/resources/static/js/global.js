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
document.addEventListener('input', e => {
    handleDateInput(e);
});