class Grid {

    constructor(rootElement, columns) {
        this.rootElement = rootElement;
        this.columns = columns;
        this.events = {};

        this.rootElement.addEventListener('click', (e) => this.clickHandler(e));
        this.rootElement.addEventListener('contextmenu', (e) => this.contextMenuHandler(e));

        this.calculateCoordinates();
    }

    clickHandler(e) {
        const $cell = e.target.closest(`[data-form="th"], [data-form="td"]`);
        if ($cell) {
            const $row = $cell.closest(`[data-form="row"]`);
            const id = $row.dataset.id;
            if (!!id) {
                this.emit('lastCheck', id);
            }
            const row = $row.dataset.row;
            const column = $cell.dataset.col;
            this.emit('click', row, column, this.getCellValue(row, column));
        }
    }

    contextMenuHandler(e) {

    }

    getCellValue(row, column) {
        const $row = this.rootElement.querySelectorAll(`[data-form="row"]`)[row];
        const $cell = $row.querySelectorAll(`[data-form="th"], [data-form="td"]`)[column];
        return $cell.dataset.form === 'td' ? $cell.querySelector(`span`).textContent : this.columns[column].header;
    }

    getCellValueByColumn(columnName) {
        return this.getCellValue(row, this.getColumnIndex(columnName));
    }

    getColumnIndex(columnName) {
        return this.columns.findIndex(column => column.name === columnName);
    }

    getActiveRows() {
        return this.rootElement.querySelectorAll(`[data-form="row"].active`);
    }

    /**
     * 주어진 이벤트 이름에 대한 이벤트 핸들러를 호출하는 메서드입니다.
     * 이 메서드는 이벤트 핸들러를 등록된 순서대로 호출하며, 각 이벤트 핸들러는 이전 이벤트 핸들러의 Promise가 완료된 후에 호출됩니다.
     * 이 메서드는 이벤트 핸들러가 Promise를 반환하도록 설계되어 있습니다.
     *
     * @param {string} eventName - 호출할 이벤트 핸들러의 이벤트 이름입니다.
     * @param {...any} args - 이벤트 핸들러에 전달할 인수입니다.
     */
    emit(eventName, ...args) {
        if (this.events[eventName]) {
            this.events[eventName].reduce((promise, handler) => {
                return promise.then(() => handler(...args));
            }, Promise.resolve());
        }
    }

    /**
     * 주어진 이벤트 이름에 이벤트 핸들러를 등록하는 메서드입니다.
     * @param {string} eventName - 이벤트 이름입니다.
     * @param {function} handler - 이벤트 핸들러 함수입니다.
     */
    on(eventName, handler) {
        if (!this.events[eventName]) {
            this.events[eventName] = [];
        }
        this.events[eventName].push(handler);
        return this;
    }

    /**
     * 모든 이벤트 핸들러를 제거하는 메서드입니다.
     */
    eventClear() {
        this.events = {};
        return this;
    }

    calculateCoordinates() {
        const $rows = this.rootElement.querySelectorAll(`[data-form="row"]`);
        $rows.forEach(($row, index) => {
            $row.dataset.row = index;

            const $columns = $row.querySelectorAll(`[data-form="th"], [data-form="td"]`);
            $columns.forEach(($column, index) => {
                $column.dataset.col = index;
            });
        });
    }

}