import React, { useCallback, useContext, useState } from 'react'
import { AuthContext } from '@Context/AuthContext'

import CreateBidForm from '@Components/CreateBidForm'
import LoadingCard from '@Components/LoadingCard'
import { buildApiUrl, createBidStream } from '@Utils/api'
import QrCodeCard from '@Components/QrCodeCard'
import MessageCard from '../MessageCard'
import { DEFAULT_ERROR } from '@Utils/generic'

const PlaceBidBtn = ({ tokenId, ownerId, successCallback }) => {
    const { authenticated, account, openModel, closeModel } = useContext(AuthContext)

    const openError = (message) => {
        openModel(<MessageCard title='Could not place bid' text={message} closeBtnCallback={closeModel} />, closeModel)
    }

    const bidSubmited = useCallback(async (price) => {
        openModel(<LoadingCard />)

        const [ref, error] = await createBidStream({ tokenId, price: Number.parseFloat(price), to: ownerId })

        if (ref === null) return openError(error?.detail || DEFAULT_ERROR.message)

        const eventSource = new EventSource(buildApiUrl('/marketplace/bids') + `?ref=${ref}`, { withCredentials : true })

        const closeModelCallback = () => {
            if(eventSource.readyState !== eventSource.CLOSED) eventSource.close()
        
            closeModel()
        }

        eventSource.addEventListener('qr', event => {
            const data = event.data

            if (data) {
                openModel(<QrCodeCard 
                    title='Scan to confirm' 
                    text='Confirm the offre by scanning this code' 
                    value={data} 
                    closeBtnCallback={closeModelCallback} />, closeModelCallback)
            }
        })

        eventSource.addEventListener('error', event => {
            const error = JSON.parse(event.data || '{}')

            if(eventSource.readyState !== eventSource.CLOSED) eventSource.close()

            openError(error.detail)
        })

        eventSource.addEventListener('accepted', () => {
            openModel(<MessageCard title='Success' text='The bid has been placed successfully' closeBtnCallback={closeModel} />, closeModel)
            
            if (typeof successCallback === 'function') successCallback(price)

            if(eventSource.readyState !== eventSource.CLOSED) eventSource.close()
        })
    }, [tokenId, ownerId, authenticated, account])

    // TODO : if user is not connected then display connect wallet before creating bid
    const placeBidCallback = () => {
        if (!authenticated || !account) return console.log("[ERROR] cannot place bid if user is not authenticated")

        openModel(<CreateBidForm onSubmitCallback={bidSubmited} />, closeModel)
    }


    return (
        <button onClick={placeBidCallback} className='PlaceBidBtn btn btn-blue block'>Place a bid</button>
    )
}

export default PlaceBidBtn