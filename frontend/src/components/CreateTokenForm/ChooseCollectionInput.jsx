import React, { useCallback, useState } from 'react'

import '@Styles/ChooseCollectionInput.css'

const ChooseCollectionInput = () => {
    const [collections, setCollections] = useState([])

    // TODO : get collection list

    const collectionSelected = (id) => {
        setCollections(collections.map(collection => {
            if (collection.id === id) collection.selected = true
            else collection.selected = false
            return collection
        }))
    }

    const generateCollection = useCallback(() => {
        return collections.map((coll, i) => (
            <div key={i} className={`collection ${coll.selected ? 'selected': ''}`} onClick={() => collectionSelected(coll.id)} >
                {coll.image 
                    ? <img src={coll.image} className='collection-image' /> 
                    : <span className='image-placeholder'></span>}

                <span className='title'>{coll.title}</span>
            </div>
        ))
    }, [collections])

    return (
        <div className='ChooseCollectionInput'>
            <label>Choose collection</label>
            <small>Choose an exiting collection or create a new one</small>

            <div className='collections-list'>
                <div className='collection add-collection-btn'>
                    <span className='image-placeholder'>
                        <i className='plus-icon'></i>
                    </span>
                    <span className='title'>Create collection</span>
                </div>
                {generateCollection()}
            </div>
        </div>
    )
}

export default ChooseCollectionInput