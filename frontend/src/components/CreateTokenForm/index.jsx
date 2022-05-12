import React, { useEffect, useState } from 'react'

import UploadFileInput from './UploadFileInput'
import ItemDetails from './ItemDetails'
import CheckboxSettingsItem from '../CheckBoxSettingsItem'
import ChooseCollectionInput from './ChooseCollectionInput'

import './styles.css'

// TODO : Maybe create a generic component for creating both single and multiple tokens
// TODO : Add form frontend validation

const CreateTokenForm = ({ onUpdateCallback, onSubmitCallback }) => {
    const [itemFile, setItemFile] = useState(null)
    const [instantSale, setInstantSale] = useState(false)
    const [isForSale, setForSale] = useState(false)
    const [price, setPrice] = useState(0)
    const [selectedCollection, setSelectedCollection] = useState(null)

    useEffect(() => {
        if (!itemFile) return

        const fileUrl = URL.createObjectURL(itemFile)

        onUpdateCallback({ previewUrl: fileUrl })
    }, [itemFile])

    useEffect(() => {
        if (price < 0) return setPrice(0)
        
        onUpdateCallback({ price })
    }, [price])

    useEffect(() => onUpdateCallback({ isForSale, instantSale }), [isForSale, instantSale])

    useEffect(() => {
        if (selectedCollection) onUpdateCallback({ collection: {...selectedCollection}})
        else onUpdateCallback({ collection: null })
    }, [selectedCollection])

    const inputChanged = (name, value) => {
        const data = {}
        data[name] = value
        onUpdateCallback(data)
    }

    const createItem = (e) => {
        e.preventDefault()
        const elements = e.target.elements

        const metadata = {
            title: elements['item-title'].value,
            description: elements['description'].value,
            isForSale: isForSale
        }

        if (instantSale && Number.parseFloat(price) > 0) {
            metadata.price = Number.parseFloat(price)
        }

        metadata.instantSale = instantSale && Number.parseFloat(price) > 0

        if (selectedCollection && selectedCollection?.id) metadata.collectionId = selectedCollection.id

        if (itemFile && metadata.title !== '' && (!instantSale || metadata?.price > 0))
            onSubmitCallback(itemFile, metadata)
    }

    return (
        <form className='CreateSingleItemForm form' onSubmit={createItem}>
            <UploadFileInput fileInputCallback={setItemFile} />

            <ItemDetails inputChangedCallback={inputChanged} />

            <hr className='horizontal-divider' />

            <div className='form-div settings'>
                <CheckboxSettingsItem onChange={e => setForSale(e.target.checked)} defaultValue={isForSale} name='for-sell' subtitle='You’ll receive bids on this item' title='Put on sale' />
                <CheckboxSettingsItem onChange={e => setInstantSale(e.target.checked)} defaultValue={instantSale} name='instant-sell' subtitle='Enter the price for which the item will be instantly sold' title='Instant sale price' />

                {instantSale ? (
                    <div className='form-subdiv'>
                        {/* <label className='form-label'>Price</label> */}
                        <input type='number' value={price} onChange={e => setPrice(e.target.value)} name='item-price' className='input-elt' min={0} step={0.001} />
                    </div>
                ) : null}

                <CheckboxSettingsItem name='unlock' subtitle='Content will be unlocked after successful transaction' title='Unlock once purchased' />
                <ChooseCollectionInput onSelectedCallback={setSelectedCollection} />
            </div>

            <button className='btn btn-blue'><span>Create item</span><i className='arrow-right-icon'></i></button>
        </form>
    )
}

export default CreateTokenForm