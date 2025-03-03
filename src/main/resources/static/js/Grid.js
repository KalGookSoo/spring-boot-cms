class Grid {

    constructor(rootElement, models, columns, options = {}) {
        this.rootElement = rootElement;
        this.models = models;
        this.columns = columns;
        this.events = {};

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
            hasColumnSort: options.hasColumnSort || false
        };

        this.build();
        this.resetMatrix();
        this.contextMenu = this.createContextMenu();

        this.rootElement.addEventListener('click', e => this.clickHandler(e));
        this.rootElement.addEventListener('contextmenu', e => this.contextMenuHandler(e));
        
        this.rootElement.addEventListener('input', e => {

        });

        this.rootElement.addEventListener('change', e => {
            const $input = e.target.closest('input, select, textarea');
            const span = $input.parentElement.querySelector(`.original-value`);
            if (span) {
                const originalValue = span.textContent;
                if (e.target.value !== originalValue) {// TODO text type이 아닌 number, datetime-local 과 같은 경우 e.target.value의 타입을 확인하고 검증해볼 것
                    this.emit('change', $input, originalValue, e.target.value);
                    e.target.closest(`[name="_action"]`).value = 'U';
                }
            }
        });
    }

    clickHandler(e) {
        const $cell = e.target.closest(`th, td`);
        if ($cell) {
            const $row = $cell.closest(`tbody tr`);
            const rowIndex = $row.dataset.rowIndex;
            const colIndex = $cell.dataset.colIndex;
            this.emit('click', rowIndex, colIndex, this.getCellValue(rowIndex, colIndex));
        }

        this.handleContextMenuBackdropClick(e);
    }

    contextMenuHandler(e) {
        e.preventDefault();
        this.contextMenu.style.left = `${e.clientX}px`;
        this.contextMenu.style.top = `${e.clientY}px`;
        this.openContextMenu();
    }

    getCellValue(rowIndex, colIndex) {
        const $row = this.rootElement.querySelectorAll(`tbody tr`)[rowIndex];
        const $cell = $row.querySelectorAll(`th, td`)[colIndex];
        return $cell.localName === 'th' || $cell.localName === 'td' ? $cell.querySelector(`input`).value : this.columns[colIndex].header;
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
        // Create Table header
        this.rootElement.createTHead();
        const headerRow = this.rootElement.tHead.insertRow();

        const th = headerRow.insertCell();
        th.textContent = 'Action';

        if (this.options.hasCheckbox) {
            const th = headerRow.insertCell();
            const checkbox = `<input type="checkbox" class="form-check-input" name="all">`;
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

            const td = tr.insertCell();
            td.innerHTML = `
                <select name="_action" class="form-select" disabled>
                    <option value="C">생성</option>
                    <option value="R" selected>읽기</option>
                    <option value="U">수정</option>
                    <option value="D">삭제</option>
                </select>
            `;

            if (this.options.hasCheckbox) {
                const td = tr.insertCell();
                const checkbox = `<input type="checkbox" class="form-check-input" name="_checked">`;
                td.insertAdjacentHTML('beforeend', checkbox);
            }

            if (this.options.hasIndex) {
                const td = tr.insertCell();
                const input = `<input type="number" min="1" class="form-control-plaintext" name="_sequence" value="${index + 1}" readonly>`;
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
                // input.classList.add('d-none');
                const span = document.createElement('span');
                span.classList.add('form-control-plaintext', 'd-none', 'original-value');
                span.textContent = model[column.name];

                td.appendChild(input);
                td.appendChild(span);
            });
        });

        this.rootElement.createTFoot();

    }

    resetMatrix() {
        const rows = this.rootElement.querySelectorAll(`tbody tr`);
        rows.forEach((row, index) => {
            row.dataset.rowIndex = index;

            const columns = row.querySelectorAll(`th, td`);
            columns.forEach((column, index) => {
                let offset = 0;
                if (this.options.hasCheckbox) {
                    offset++;
                }
                if (this.options.hasIndex) {
                    offset++;
                }
                column.dataset.colIndex = index;
            });
        });
    }

    createContextMenu() {
        const dialog = document.createElement('dialog');
        dialog.classList.add('grid-context-menu');
        const ul = `
            <ul class="list-group">
                <li class="list-group-item"><button type="button" class="btn" name="add">추가</button></li>
                <li class="list-group-item"><button type="button" class="btn" name="delete">삭제</button></li>
                <li class="list-group-item"><button type="button" class="btn" name="save">저장</button></li>
            </ul>
        `;
        dialog.insertAdjacentHTML('beforeend', ul);
        this.rootElement.appendChild(dialog);
        return dialog;
    }

    openContextMenu() {
        this.contextMenu.showModal();
    }

    handleContextMenuBackdropClick(e) {
        e.target === this.contextMenu && this.closeContextMenu();
    }

    closeContextMenu() {
        this.contextMenu.close();
    }

    addRow(index = 0) {
        // this.columns를 순회하여 row를 구성한다.
        const tr = this.rootElement.tBodies[0].insertRow(index);

        const td = tr.insertCell();
        td.innerHTML = `
                <select name="_action" class="form-select" disabled>
                    <option value="C" selected>생성</option>
                    <option value="R">읽기</option>
                    <option value="U">수정</option>
                    <option value="D">삭제</option>
                </select>
            `;

        if (this.options.hasCheckbox) {
            const td = tr.insertCell();
            const checkbox = `<input type="checkbox" class="form-check-input" name="_checked">`;
            td.insertAdjacentHTML('beforeend', checkbox);
        }

        if (this.options.hasIndex) {
            const td = tr.insertCell();
            const input = `<input type="number" min="1" class="form-control-plaintext" name="_sequence" readonly>`;
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
            const formControlStyle = column.editable ? 'form-control' : 'form-control-plaintext';
            input.classList.add(formControlStyle);
            td.appendChild(input);
        });
        this.resetMatrix();
    }

    getRowsByAction(action) {
        const selectElements = this.rootElement.querySelectorAll(`tbody tr select[name="_action"]`);
        return [...selectElements].filter(select => select.value === action).map(select => select.closest('tr'));
    }

    getModelsByAction(action) {
        const rowsByAction = this.getRowsByAction(action);
        return rowsByAction.map(row => {
            return [...row.querySelectorAll('input, select, textarea')].reduce((acc, element) => {
                acc[element.name] = element.value;
                return acc;
            }, {});
        });
    }

    getModelsByChecked(checked) {
        const inputs = this.rootElement.querySelectorAll(`tbody tr input[name="_checked"]`);
        return [...inputs].filter(input => input.checked === checked)
            .map(input => {
                const tr = input.closest('tr');
                return [...tr.querySelectorAll('input, select, textarea')].reduce((acc, element) => {
                    acc[element.name] = element.value;
                    return acc;
                }, {});
            });
    }

    getModels(tr) {

    }

}