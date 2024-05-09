const createDialog = (title, body) => {
    const popup = `
                <div class="modal" tabIndex="-1">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">${title}</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                ${body}
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                <button type="button" class="btn btn-primary">Save changes</button>
                            </div>
                        </div>
                    </div>
                </div>
            `;

    document.body.insertAdjacentHTML('beforeend', popup);
    const $modal = document.body.querySelector(`.modal`);
    $modal.addEventListener('hidden.bs.modal', e => {
        $modal.remove();
    });
    new bootstrap.Modal($modal).show();
};

const attachmentPopup = () => {
    const body = `
        <form>
            <table id="users" class="table table-hover border">    
                <thead>
                    <tr></tr>
                    <tr>#</tr>
                </thead>
                <tbody>
                    <tr>
                        <td colspan="100%" class="text-center"></td>
                    </tr>
                </tbody>
            </table>
        </form>
    `;
    createDialog('Attachment', 'This is the attachment popup');
}