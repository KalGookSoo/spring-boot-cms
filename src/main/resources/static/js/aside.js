document.addEventListener('DOMContentLoaded', e => {
    document.querySelector(`#aside`);
    createDragElement(navElement);
});

const createDragElement = (element) => {
    let startX, startWidth;

    const doDrag = (e) => {
        element.style.width = (startWidth + e.clientX - startX) + 'px';
    }

    const stopDrag = () => {
        document.documentElement.removeEventListener('mousemove', doDrag, false);
        document.documentElement.removeEventListener('mouseup', stopDrag, false);
    }

    const initDrag = (e) => {
        startX = e.clientX;
        startWidth = parseInt(document.defaultView.getComputedStyle(element).width, 10);
        document.documentElement.addEventListener('mousemove', doDrag, false);
        document.documentElement.addEventListener('mouseup', stopDrag, false);
    }

    // Sidebar element
    const resizer = document.createElement('div');
    resizer.style.width = '5px';
    resizer.style.marginRight = '-5px';
    resizer.style.height = '100vh';
    resizer.style.background = '#000';
    resizer.style.position = 'absolute';
    resizer.style.cursor = 'ew-resize';
    resizer.style.right = '0';
    resizer.style.top = '0';
    resizer.addEventListener('mousedown', initDrag, false);
    element.style.position = 'relative';
    element.appendChild(resizer);
}