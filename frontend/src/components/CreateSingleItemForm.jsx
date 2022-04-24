import React, { useState } from 'react'

import UploadFileInput from './UploadFileInput'
import ItemDetails from './ItemDetails'

import '../styles/CreateSingleItemForm.css'

// TODO : Maybe create a generic component for creating both singles and multiples

const CreateSingleItemForm = () => {
    const [itemFile, setItemFile] = useState(null)

    const createItem = (e) => {
        e.preventDefault()
    }

    return (
        <form className='CreateSingleItemForm form' onSubmit={createItem}>
            <UploadFileInput fileInputCallback={setItemFile} />

            <ItemDetails />
        </form>
    )
}

export default CreateSingleItemForm