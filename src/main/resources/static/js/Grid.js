class Grid {

    constructor(rootElement, models, columns, options = {}) {
        if (!this.rootElement instanceof HTMLTableElement) {
            throw new Error('rootElement must be an instance of HTMLTableElement');
        }
        this.rootElement = rootElement;
        this.models = models;
        this.columns = columns;
        this.events = {};

        this.rootElement.addEventListener('click', (e) => this.clickHandler(e));
        this.rootElement.addEventListener('contextmenu', (e) => this.contextMenuHandler(e));

        this.options = {
            hasCheckbox: options.hasCheckbox || false,
            hasIndex: options.hasIndex || false,
            hasRowNumber: options.hasRowNumber || false,
            hasRowMenu: options.hasRowMenu || false,
            hasToolbar: options.hasToolbar || false,
            hasDetail: options.hasDetail || false,
            hasFilter: options.hasFilter || false,
            hasPagination: options.hasPagination || false,
            hasColumnFilter: options.hasColumnFilter || false,
            hasColumnSort: options.hasColumnSort || false,
        };


    }

    clickHandler(e) {
        const $cell = e.target.closest(`th, td`);
        if ($cell) {
            const $row = $cell.closest(`tbody tr`);
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
        const $row = this.rootElement.querySelectorAll(`tbody tr`)[row];
        const $cell = $row.querySelectorAll(`th, td`)[column];
        return $cell.dataset.form === 'td' ? $cell.querySelector(`span`).textContent : this.columns[column].header;
    }

    getCellValueByColumn(columnName) {
        return this.getCellValue(row, this.getColumnIndex(columnName));
    }

    getColumnIndex(columnName) {
        return this.columns.findIndex(column => column.name === columnName);
    }

    getActiveRows() {
        return this.rootElement.querySelectorAll(`tbody tr.active`);
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

    build() {
        // createHeaders();
        // createBodies();

        // Create Table header
        this.rootElement.createTHead();
        const headerRow = this.rootElement.tHead.insertRow();
        if (this.options.hasCheckbox) {
            const th = headerRow.insertCell();
            const checkbox = `<input type="checkbox" name="all">`;
            th.insertAdjacentHTML('beforeend', checkbox);
        }

        if (this.options.hasIndex) {
            const th = headerRow.insertCell();
            th.textContent = 'No';
        }
        this.columns.forEach(column => {
            const th = headerRow.insertCell();
            if (column.hidden) {
                th.classList.add('d-none');
            }
            th.textContent = column.header;
        });

        // Create Table body
        this.rootElement.createTBody();
        const tbody = this.rootElement.tBodies[0];
        this.models.forEach((model, index) => {
            const tr = tbody.insertRow();
            if (this.options.hasCheckbox) {
                const td = tr.insertCell();
                const checkbox = `<input type="checkbox" name="">`;
                td.insertAdjacentHTML('beforeend', checkbox);
            }

            if (this.options.hasIndex) {
                const td = tr.insertCell();
                const input = `<input type="number" min="1" class="form-control-plaintext" value="${index + 1}" readonly>`;
                td.insertAdjacentHTML('beforeend', input);
            }

            this.columns.forEach(column => {
                const td = tr.insertCell();
                if (column.hidden) {
                    td.classList.add('d-none');
                }
                const input = document.createElement('input');
                input.type = column.type;
                input.name = column.name;
                input.value = model[column.name];
                const formControlStyle = column.editable ? 'form-control' : 'form-control-plaintext';
                input.classList.add(formControlStyle);
                td.appendChild(input);
            });
        });

        this.rootElement.createTFoot();
        this.calculateCoordinates();
    }

    calculateCoordinates() {
        const $rows = this.rootElement.querySelectorAll(`tbody tr`);
        $rows.forEach(($row, index) => {
            $row.dataset.row = index;

            const $columns = $row.querySelectorAll(`th, td`);
            $columns.forEach(($column, index) => {
                $column.dataset.col = index;
            });
        });
    }


}