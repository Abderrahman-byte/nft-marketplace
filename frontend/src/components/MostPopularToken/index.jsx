import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'

import PlaceBidBtn from '@Components/PlaceBidBtn'
import { convertRvnToUsd, formatMoney } from '@Utils/currency'

import './styles.css'

// TODO : make profile element into its own component

const MostPopularToken = ({id, previewUrl, title, creator, collection, owner, highestBid }) => {
    const [usdPrice, setUsdPrice] = useState(0)
    const [localHighestBid, setLocalHighestBid] = useState(highestBid?.price)

    const updateUsdPrice = async () => {
        if (!localHighestBid) return

        const price = await convertRvnToUsd(localHighestBid || 0)

        setUsdPrice(price)
    }

    const onPlaceBidSuccess = (price) => {
        if (Number(price) > Number(localHighestBid)) setLocalHighestBid(price)
    }

    useEffect(() => {
        updateUsdPrice()
    }, [localHighestBid])

    useEffect(() => {
        if(highestBid && highestBid?.price) setLocalHighestBid(highestBid?.price)
    }, [highestBid])

    const getProfileElt = (title, img, name, to = '#') => {
        return (
            <div className='profile-elt'>
                <Link className='block' to={to}>
                    <img src={img} />
                </Link>
                <div className='profile-elt-info'>
                    <h6>{title}</h6>
                    <span className='name'>{name}</span>
                </div>
            </div>
        )
    }

    return (
        <div className='MostPopularToken'>
            <img src={previewUrl} />
            <div className='info'>
                <h3>{title}</h3>

                <div className='profiles'>
                    {getProfileElt('Creator', creator?.avatarUrl, creator?.displayName, `/user/${creator?.id}`)}
                    {collection ? (
                        getProfileElt('Collection', collection?.imageUrl, collection?.name, `/collection/${collection?.id}`)
                    ) : null}
                </div>
                
                {localHighestBid ? (
                    <div className='bid-panel'>
                        <h6>Current Bid</h6>
                        <span className='rvn'>{formatMoney(localHighestBid || 0)} RVN</span>
                        <span className='dollar'>${formatMoney(usdPrice)}</span>
                        {/* <h6>Auction ending in</h6>
                        <Timer date={new Date()} /> */}
                    </div>
                ): null}

                <div className='buttons'>
                    <PlaceBidBtn tokenId={id} ownerId={owner?.id || creator?.id} successCallback={onPlaceBidSuccess} />
                    <Link to={`/details/${id}`} className='btn btn-white block'>View NFT</Link>
                </div>
            </div>
        </div>
    )
}

export default MostPopularToken