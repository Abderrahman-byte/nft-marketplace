import React, { useContext, useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'

import HeartIconSVG from '@/components/HeartIconSVG'
import { deleteLikeToken, postLikeToken } from '@/utils/api'
import { AuthContext } from '@/context/AuthContext'
import { ONE_DAY } from '@/utils/generic'
import { formatMoney } from '@/utils/currency'

import './styles.css'

const TokenCard = ({ id, title, price, creator, owner, collection, previewUrl, liked, createdDate, instantSale, highestBid, likable = false, link = false, style={}, ...rest }) => {
    const { account } = useContext(AuthContext)
    const [isLiked, setLiked] = useState(liked)
    const [isLoading, setLoading] = useState(false)

    useEffect(() => setLiked(liked), [liked])

    console.log(createdDate)

    const likeBtnClicked = async (e) => {
        e.preventDefault()

        if (isLoading) return

        setLoading(true)

        const done = isLiked ? await deleteLikeToken(id) : await postLikeToken(id)
        
        if (done) setLiked(!isLiked)

        setLoading(false)
    }

    const getChildren = () => {
        return (
            <>
                <div className='token-img' style={{ 'backgroundImage': `url(${previewUrl}`}} >
                    {instantSale ? (
                        <span className='purshace-tag'>purshacing!</span>
                    ) : null}

                    {likable && account ? (
                        <button onClick={likeBtnClicked} className={`like-btn ${isLiked ? 'liked' : ''}` } >
                            <HeartIconSVG className='heart' />
                        </button>
                    ) : null}
                </div>
                <div className='info'>
                    <div className='info-div'>
                        <h3>{title}</h3>
                        {price > 0 ? (
                            <span className='price'>{formatMoney(price)} RVN</span>
                        ) : null}
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
                        {highestBid ? (
                            <div className='flex'><i className='candle-sticks-icon'></i><span>higest bid</span><span className='bold'>{formatMoney(highestBid?.price)} RVN</span></div>
                        ) : null}

                        {Date.now() - createdDate <= ONE_DAY ? (
                            <span className='new-bid-span'>New bid 🔥</span>
                        ) : null}
                    </div>
                </div>
            </>
        )
    }

    if (link) return <Link style={{...style}} to={`/details/${id}`} className='TokenCard'>{getChildren()}</Link>

    return <div style={{...style}} className='TokenCard'>{getChildren()}</div>
}

export default TokenCard