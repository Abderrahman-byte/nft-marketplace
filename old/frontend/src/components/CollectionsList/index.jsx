import React from 'react'

import CollectionCard from '@Components/CollectionCard'

import  './styles.css'

// TODO : This components should replace most of 

const CollectionsList = ({ data = [] }) => {
    return (
        <div className='CollectionsList'>
            {data.map((collection, i) => <CollectionCard key={i} link {...collection} />)}
        </div>
    )
}

export default CollectionsList