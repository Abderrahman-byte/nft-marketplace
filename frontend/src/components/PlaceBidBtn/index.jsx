import React, { useCallback, useContext } from 'react'
import { AuthContext } from '@/context/AuthContext'

import CreateBidForm from '../CreateBidForm'
import LoadingCard from '../LoadingCard'
import { createBidStream } from '@/utils/api'
import QrCodeCard from '../QrCodeCard'

const PlaceBidBtn = ({ tokenId, ownerId }) => {
    const { authenticated, account, openModel, closeModel } = useContext(AuthContext)

    const bidSubmited = useCallback(async (price) => {
        openModel(<LoadingCard />)

        const ref = await createBidStream({ tokenId, price: Number.parseFloat(price), to: ownerId })

        console.log(ref)

        closeModel()
    }, [tokenId, ownerId, authenticated, account])

    // TODO : if user is not connected then display connect wallet before creating bid
    const placeBidCallback = () => {
        if (!authenticated || !account) return console.log("[ERROR] cannot place bid if user is not authenticated")

        openModel(<CreateBidForm onSubmitCallback={bidSubmited} />, closeModel)
    }

    openModel(<QrCodeCard />)

    return (
        <button onClick={placeBidCallback} className='PlaceBidBtn btn btn-blue block'>Place a bid</button>
    )
}

export default PlaceBidBtn