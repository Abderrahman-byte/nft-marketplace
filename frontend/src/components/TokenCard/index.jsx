import React, { useContext, useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'

import HeartIconSVG from '@/components/HeartIconSVG'
import { deleteLikeToken, postLikeToken } from '@/utils/api'
import { AuthContext } from '@/context/AuthContext'

import './styles.css'

const TokenCard = ({ id, title, price, creator, owner, collection, previewUrl, liked, likable = false, link = false, style={} }) => {
    const { account } = useContext(AuthContext)
    const [isLiked, setLiked] = useState(liked)
    const [isLoading, setLoading] = useState(false)

    useEffect(() => {
        setLiked(liked)
    }, [liked])

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
                    {likable && account ? (
                        <button onClick={likeBtnClicked} className={`like-btn ${isLiked ? 'liked' : ''}` } >
                            <HeartIconSVG className='heart' />
                        </button>
                    ) : null}
                </div>
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
            </>
        )
    }

    if (link) return <Link style={{...style}} to={`/details/${id}`} className='TokenCard'>{getChildren()}</Link>

    return <div style={{...style}} className='TokenCard'>{getChildren()}</div>
}

export default TokenCard