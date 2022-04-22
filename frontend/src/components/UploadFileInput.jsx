import React from 'react'

const UploadFileInput = () => {
    return (
        <div className='UploadFileInput form-div'>
            <label>Upload File</label>
            <small>Drag or choose your file to upload</small>

            <input type='file' name='item' />

            <button className='upload-btn'>
                <i className='upload-icon'></i>
                <p>PNG, GIF, WEBP, MP4 or MP3, Max 1Gb.</p>
            </button>
        </div>
    )
}

export default UploadFileInput