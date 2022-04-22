import React from 'react'

import '../styles/CreateSingleItemForm.css'
import UploadFileInput from './UploadFileInput'

// TODO : Maybe create a generic component for creating both singles and multiples

const CreateSingleItemForm = () => {
    const createItem = (e) => {
        e.preventDefault()
    }

    return (
        <form className='CreateSingleItemForm form' onSubmit={createItem}>
            <UploadFileInput />
        </form>
    )
}

export default CreateSingleItemForm