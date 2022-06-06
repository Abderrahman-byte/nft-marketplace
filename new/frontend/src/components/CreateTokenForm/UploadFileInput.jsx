import React, { createRef } from 'react'

const UploadFileInput = ({fileInputCallback}) => {
    const inputRef = createRef()
    const acceptedFiles = /^(image|video|audio)+\/(png|gif|webp|mp4|mp3|jpeg)+$/
    const MAX_SIZE = Math.pow(1024, 3)

    const btnClickedCallback = () => {
        if (inputRef.current) inputRef.current.click()
    }

    const fileInserted = file => {
        // TODO : errors must be displayed for the user later
        if (!file || !acceptedFiles.test(file.type) || file.size > MAX_SIZE) return

        if (typeof fileInputCallback === 'function') fileInputCallback(file)
    }

    const dropCallback = (e) => {
        let file = null
        e.stopPropagation()
        e.preventDefault()

        
        if (e.dataTransfer.items && e.dataTransfer.items.length > 0) {
            for (let i = 0; i < e.dataTransfer.items.length; i++) {
                if (e.dataTransfer.items[i].kind === 'file') {
                    file = e.dataTransfer.items[i].getAsFile()
                    break;
                }
            }

        } else if (e.dataTransfer.files.length > 0) {
            file = e.dataTransfer.files[0]
        }

        fileInserted(file) 
    }

    const fileInputChanged = (e) => {
        const files = e.target.files
        if (files.length > 0) fileInserted(files[0])
    }

    const dragCallback = (e) => {
        e.preventDefault()
        e.stopPropagation()
    }

    return (
        <div className='UploadFileInput form-div'>
            <label className='section-label'>Upload File</label>
            <small>Drag or choose your file to upload</small>

            <input onChange={fileInputChanged} ref={inputRef} type='file' name='item' />

            <button type='button' onDrag={dragCallback} onDrop={dropCallback} onClick={btnClickedCallback} className='upload-btn'>
                <i className='upload-icon'></i>
                <p>PNG, GIF, WEBP, MP4 or MP3, Max 1Gb.</p>
            </button>
        </div>
    )
}

export default UploadFileInput