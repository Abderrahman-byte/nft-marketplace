import React, { useEffect, useMemo, useState } from 'react'
import { useParams } from 'react-router'

import TokensList from '@Components/TokensList'
import { getCollectionDetails, getCollectionTokens } from '@Utils/api'

import '@Styles/CollectionPage.css'

const CollectionPage = ({ }) => {
    const params = useParams()
    const id = useMemo(() => params?.id, [params])
    const [page, setPage] = useState(1)
    const [items, setItems] = useState([])
    const [details, setDetails] = useState(undefined)
    const [isLoading, setLoading] = useState(false)
    const [itemsPerPage, setItemsPerPage] = useState(10)

    const fetchCollectionDetails = async () => {
        if (isLoading) return

        setLoading(true)
        const data = await getCollectionDetails(id)
        
        if (!data) return setDetails(null)

        setDetails(data)
        setItems(data?.items || [])
        setPage((data?.items || []).length < data?.itemsCount ? 1 : 0)
        setItemsPerPage((data?.items || []).length)
        setLoading(false)
    }

    const fetchItems = async () => {
        if (page <= 0 || !itemsPerPage || itemsPerPage <= 0 || isLoading) return

        setLoading(true)
        const data = await getCollectionTokens(id, itemsPerPage, items.length)

        if (!data || data.length < itemsPerPage) setPage(0)

        setItems(items => setItems([...items, ...data]))
        setLoading(false)
    }

    useEffect(() => {
        if (page <= 1) return 

        fetchItems()
    }, [page])

    useEffect(() => {
        setItems([])
        setDetails(undefined)
        setItemsPerPage(10)
        setPage(1)
        fetchCollectionDetails()
    }, [id])

    if (!details) return (<></>)

    return (
        <div className='CollectionPage'>
            <div className='CollectionPage-header'>
                <img src={details?.imageUrl} />
            </div>
            <div className='CollectionPage-info'>
                <h3 className='name'>{details?.name}</h3>
                {details?.description ? <p className='description'>{details.description}</p> : null}
            </div>

            <div className='page'>
                <TokensList data={items} />

                {page > 0 && !isLoading ? (
                    <button onClick={() => setPage(p => p + 1)} className='btn btn-blue block more-btn'>More</button>
                ) : null}
            </div>
        </div>
    )
}

export default CollectionPage