import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'

import PlaceBidBtn from '@Components/PlaceBidBtn'
import { convertRvnToUsd, formatMoney } from '@Utils/currency'

import './styles.css'
import AvatarLink from '@Components/AvatarLink'

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

    return (
        <div className='MostPopularToken'>
            <img src={previewUrl} />
            <div className='info'>
                <h3>{title}</h3>

                <div className='profiles'>
                    <AvatarLink title='Creator' img={creator?.avatarUrl} name={creator?.displayName} to={`/user/${creator?.id}`} />
                    {collection ? (
                        <AvatarLink title='Collection' img={collection?.imageUrl} name={collection?.name} to={`/collection/${collection?.id}`} />
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