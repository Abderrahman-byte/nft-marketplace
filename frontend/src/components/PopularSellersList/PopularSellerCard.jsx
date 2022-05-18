import React from 'react'
import { Link } from 'react-router-dom'

import { formatMoney } from '@Utils/currency'

const PopularSellerCard = ({ displayName, totalTransactions, id, avatarUrl, rank=6 }) => {
    return (
        <div className='PopularSellerCard'>
            <div className='PopularSellerCard-head'>
                <span className={`trophy trophy-${rank}`}>
                    <i className={`badge-rank-${rank}`}></i>
                    <span>#{rank}</span>
                </span>
                <Link to={`/user/${id}`}>
                    <i className='link-arrow'></i>
                </Link>
            </div>
            
            <div className='horizontal-divider'></div>

            <div className='info'>
                {avatarUrl ? (
                    <img src={avatarUrl} className='avatar' />
                ) : (
                    <div className='avatar'></div>
                )}

                <p className='name'>{displayName}</p>
                <p className='total'>{formatMoney(totalTransactions)} <span className='currency'>RVN</span></p>
            </div>
        </div>
    )
}

export default PopularSellerCard