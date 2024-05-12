class FileUploader {

    #el;

    constructor() {
        document.body.insertAdjacentHTML('beforeend', this.template());
        this.#el = document.querySelector(`.file-uploader`);
        this.eventBinding();
    }



    add(e) {
        // TODO `.file-uploader .btn-add` click event handler
        // TODO input type="file"을 클릭했을 때 처럼 첨부파일 창을 출력합니다.
        // TODO 첨부한 파일을 `.file-uploader .table의 tr을 추가하여 추가된 tr에 input type="file"로써 추가합니다.`

        const $input = document.createElement('input');
        $input.type = 'file';
        $input.style.display = 'none';

        document.body.appendChild($input);

        $input.click();

        $input.onchange = event => {
            const file = event.target.files[0];

            const $table = document.querySelector(`.file-uploader .table`);
            const $row = $table.insertRow();

            const $checkboxCell = $row.insertCell(0);
            const $nameCell = $row.insertCell(1);
            const $sizeCell = $row.insertCell(2);

            $checkboxCell.innerHTML = `<input type="checkbox"/>`;
            $nameCell.innerHTML = file.name;
            $sizeCell.innerHTML = file.size;

            document.body.removeChild($input);
        };
    }

    remove(e) {
        // TODO `.file-uploader .btn-remove` click event handler
        // TODO remove input element by checked input
    }

    save(e) {
        // TODO `.file-uploader .btn-save` click event handler
    }

    template() {// deprecated
        return `
            <form class="file-uploader">
                <div>
                    <button type="button" class="btn btn-primary mt-2 btn-add"></button>
                    <button type="button" class="btn btn-primary mt-2 btn-remove"></button>
                    <button type="button" class="btn btn-primary mt-2 btn-save"></button>
                </div>
                <table class="table table-hover border">    
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>name</th>
                            <th>size</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><input type="checkbox"/></td>
                            <td>-</td>
                            <td>-</td>
                        </tr>
                    </tbody>
                </table>
            </form>
        `;
    }

    create() {
        const form = document.createElement('form');
        const header = document.createElement('section');
        const addButton = document.createElement('button');
        addButton.type = 'button';
        addButton.className = `btn btn-outline-primary mt-2 btn-add`;
        const removeButton = document.createElement('button');
        removeButton.type = 'button';
        removeButton.className = `btn btn-danger mt-2 btn-remove`;
        const saveButton = document.createElement('button');
        saveButton.type = 'button';
        saveButton.className = `btn btn-primary mt-2 btn-save`;

        const body = document.createElement('section');
        const table = new HTMLTableElement();
        const columns = ['checked', 'name', 'size'];

        const footer = document.createElement('section');

    }

    eventBinding() {
        this.#el.querySelector(`.btn-add`).addEventListener('click', e => this.add(e));
        this.#el.querySelector(`.btn-remove`).addEventListener('click', e => this.remove(e));
        this.#el.querySelector(`.btn-save`).addEventListener('click', e => this.save(e));
    }

}