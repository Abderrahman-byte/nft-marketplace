import React, { useState } from 'react'

import UploadFileInput from './UploadFileInput'
import ItemDetails from './ItemDetails'
import CheckboxSettingsItem from './CheckboxSettingsItem'

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

            <hr className='horizontal-divider' />

            <div className='form-div settings'>
                <CheckboxSettingsItem defaultValue name='for-sell' subtitle='You’ll receive bids on this item' title='Put on sale' />
                <CheckboxSettingsItem name='instant-sell' subtitle='Enter the price for which the item will be instantly sold' title='Instant sale price' />
                <CheckboxSettingsItem name='unlock' subtitle='Content will be unlocked after successful transaction' title='Unlock once purchased' />
            </div>
        </form>
    )
}

export default CreateSingleItemForm