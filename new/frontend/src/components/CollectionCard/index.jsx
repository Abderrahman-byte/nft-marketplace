import React from 'react'
import { Link } from 'react-router-dom'

import './styles.css'

const CollectionCard = ({ id, imageUrl, link = false, items, name, creator, itemsCount }) => {
    const getChildren = () => {
        return (
            <>
                <img className='collection-image' src={imageUrl} />

                <div className='first-items'>
                    {items?.slice(0,3).map((token, i) => <img key={i} className='token-img' src={token?.previewUrl} />)}
                </div>

                <div className='info'>
                    <h4>{name}</h4>

                    <div className='info-details'>
                        <div className='creator-info'>
                            <Link className='block' to={`/user/${creator?.id}`}> 
                                <img src={creator.avatarUrl} />
                            </Link>
                            <span className='creator-name'>By {creator.displayName || creator.username}</span>
                        </div>

                        <span className='items-count'>{itemsCount} items</span>
                    </div>
                </div>
            </>
        )
    }

    if (link) return (
        <Link className='CollectionCard block' to={`/collection/${id}`}>
            {getChildren()}
        </Link>
    )

    return (
        <div className='CollectionCard'>
            {getChildren()}
        </div>
    )
}

export default CollectionCard