import React from 'react'
import { Route, Routes } from 'react-router'

import UploadItemPage from './UploadItem.page'
import UploadSinglePage from './UploadSingle.page'

const UploadPages = () => {
    return (
        <Routes>
            <Route index element={<UploadItemPage />} />
            <Route path='single' element={<UploadSinglePage />} />
        </Routes>
    )
}

export default UploadPages