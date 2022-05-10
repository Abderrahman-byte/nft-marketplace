import React from 'react'
import { Link } from 'react-router-dom'

import PlaceBidBtn from '@Components/PlaceBidBtn'
import Timer from '@Components/Timer'

import './styles.css'

// TODO : make profile element into its own component

const MostPopularToken = ({id, previewUrl, title, creator, collection, owner }) => {
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

                <div className='bid-panel'>
                    <h6>Current Bid</h6>
                    <span className='rvn'>10,000 RVN</span>
                    <span className='dollar'>$3,618.36</span>
                    <h6>Auction ending in</h6>
                    <Timer date={new Date()} />
                </div>

                <div className='buttons'>
                    <PlaceBidBtn tokenId={id} ownerId={owner?.id || creator?.id} />
                    <Link to='#' className='btn btn-white block'>View NFT</Link>
                </div>
            </div>
        </div>
    )
}

export default MostPopularToken