import React, { useEffect, useRef, useState } from 'react'

import './styles.css'

const CreateCollectionForm = ({ defaultData, defaultErrors, onSubmitCallback, defaultImage }) => {
    const [image, setImage] = useState()
    const [errors, setErrors] = useState([])
    const imageInput = useRef()

    useEffect(() => {
        if (defaultData && !image && defaultData?.image) setImage(defaultData.image)
    }, [defaultData, image])

    useEffect(() => {
        if (defaultErrors) setErrors(defaultErrors)
    }, [defaultErrors])

    useEffect(() => {
        if (defaultImage && imageInput.current && !image) {
            const container = new DataTransfer()
            const file = new File([defaultImage], defaultImage.name)
            container.items.add(file)
            imageInput.current.files = container.files
        }
    }, [defaultImage, image])

    const imageFileChanged = (e) => {
        const files = e.target.files

        if (!files || files.length <= 0) return

        const fileUrl = URL.createObjectURL(files[0])

        setImage(fileUrl)
    }

    const submitCallback = (e) => {
        const elements = e.target.elements
        const formData = new FormData()
        e.preventDefault()

        if (!elements['name'].value || elements['name'].value.length < 3) {
            setErrors([{ message: 'Display field is required and must be at least 3 characters long.', field: 'name' }])
        }

        const data = {
            name: elements['name'].value,
            description: elements['description'].value
        }

        if (elements['image'].files.length >= 0) 
            formData.append('image', elements['image'].files[0])

        formData.append('metadata', JSON.stringify(data))
        data.imageUrl = image

        if (typeof onSubmitCallback === 'function') onSubmitCallback(formData, data)
    }

    return (
        <form onSubmit={submitCallback} className='CreateCollectionForm form form-card card'>
            <div className='form-div'>
                <h2>Create Collection</h2>
            </div>

            <div className='form-div'>
                <img src={image} alt='collection' />
                <div>
                    <span>We recommend an image of at least 300x300. Gifs work too.<br/>Max 5mb.</span>
                    <label htmlFor='collection-image-input' className='btn btn-white block'>Choose file</label>
                    <input onChange={imageFileChanged} ref={imageInput} className='hidden' id='collection-image-input' name='image' type='file' accept='image/*' />
                </div>
            </div>

            <div className='form-div'>
                <label className='form-label'>Name</label>
                <input onChange={() => setErrors([])} defaultValue={defaultData?.name || ''} name='name' className='form-input' type='text' placeholder='Display name' />
            </div>

            <div className='form-div'>
                <label className='form-label'>Name</label>
                <textarea name='description' className='form-input' defaultValue={defaultData?.description || ''} placeholder='description' />
            </div>

            { errors && errors.length > 0 ? (
                <div className='form-div errors-div'>
                    {errors.map((err, i) => <span key={i}>{err.message}</span>)}
                </div>
            ): null}

            <button className='btn btn-blue'>Create Collection</button>
        </form>
    )
}

export default CreateCollectionForm