import React from 'react'
import { Link } from 'react-router-dom'

import './styles.css'

const TokenCard = ({ title, price, creator, owner, collection, previewUrl }) => {
    return (
        <Link to='#' className='TokenCard'>
            <div className='token-img' style={{ 'backgroundImage': `url(${previewUrl}`}} />
            <div className='info'>
                <div className='info-div'>
                    <h3>{title}</h3>
                    <span className='price'>{price} RVN</span>
                </div>

                <div className='info-div'>
                    <div className='avatars'>
                        {owner ? (
                            <Link to={`/user/${owner.id}`}>
                                <img src={owner.avatarUrl} />
                            </Link>
                        ) : null}

                        {collection ? (
                            <Link to={`/collection/${collection.id}`}>
                                <img src={collection.imageUrl} />
                            </Link>
                        ) : null}

                        {creator ? (
                            <Link to={`/user/${creator.id}`}>
                                <img src={creator.avatarUrl} />
                            </Link>
                        ) : null}
                    </div>
                    <span className='quantity'>1 in stock</span>
                </div>

                <div className='horizontal-divider' />

                <div className='info-div info-footer'>
                    <div className='flex'><i className='candle-sticks-icon'></i><span>higest bid</span><span className='bold'>0.001 RVN</span></div>
                    <span>New bid 🔥</span>
                </div>
            </div>
        </Link>
    )
}

export default TokenCard