import React from 'react';
import { Link } from 'react-router-dom';

function Sidebar() {
    return (
        <div style={sidebarStyle}>
            <h2 style={titleStyle}>관리자 메뉴</h2>
            <ul style={listStyle}>
                <li>
                    <h3 style={subtitleStyle}>새 강의 등록</h3>
                    <ul style={subListStyle}>
                        <li><Link to="/admin/courses/create" style={linkStyle}>강의 등록</Link></li>
                    </ul>
                </li>
                <li>
                    <h3 style={subtitleStyle}>강의 관리</h3>
                    <ul style={subListStyle}>
                        <li><Link to="/admin/courses-management" style={linkStyle}>동영상 관리 (등록/삭제)</Link></li>
                        <li><Link to="/admin/news-management" style={linkStyle}>새소식 관리 (등록/수정/삭제)</Link></li>
                        <li><Link to="/admin/inquiry-management" style={linkStyle}>강의 문의 관리</Link></li>
                        <ul style={subListStyle}>
                            <li><Link to="/admin/inquiry-management/view" style={linkStyle}>강의 문의 조회</Link></li>
                            <li><Link to="/admin/inquiry-management/edit" style={linkStyle}>강의 문의 수정</Link></li>
                            <li><Link to="/admin/inquiry-management/reply" style={linkStyle}>강의 문의 답변</Link></li>
                        </ul>
                    </ul>
                </li>

                <li>
                    <h3 style={subtitleStyle}>주문 관리</h3>
                    <ul style={subListStyle}>
                        <li><Link to="/admin/order-management" style={linkStyle}>전체 주문 조회</Link></li>
                    </ul>
                </li>

                <li>
                    <h3 style={subtitleStyle}>문의 관리</h3>
                    <ul style={subListStyle}>
                        <li><Link to="/admin/customer-inquiry/new" style={linkStyle}>새 문의</Link></li>
                        <li><Link to="/admin/customer-inquiry/answered" style={linkStyle}>답변 완료 문의</Link></li>
                        <li><Link to="/admin/customer-inquiry/resolved" style={linkStyle}>해결된 문의</Link></li>
                    </ul>
                </li>

                <li>
                    <h3 style={subtitleStyle}>회원 관리</h3>
                    <ul style={subListStyle}>
                        <li><Link to="/admin/customer-inquiry/new" style={linkStyle}>회원 조회 (회원 정보 조회, 회원 삭제)</Link></li>
                        <li><Link to="/admin/customer-inquiry/answered" style={linkStyle}>강사 조회 (강사로 등록, 삭제)</Link></li>
                        <li><Link to="/admin/customer-inquiry/answered" style={linkStyle}>회원 정보 수정</Link></li>
                    </ul>
                </li>
            </ul>
        </div>
    );
}

const sidebarStyle = {
    width: '200px',
    backgroundColor: '#f4f4f4',
    padding: '20px',
    position: 'relative', // 사이드바가 일반적인 레이아웃 요소로 배치되도록 설정
    top: 0, // fixed가 아니므로 top을 설정할 필요가 없습니다
    height: '100%', // 사이드바의 높이를 컨텐츠에 맞추도록 설정
};


const titleStyle = {
    fontSize: '18px',
    fontWeight: 'bold',
    marginBottom: '20px',
};

const subtitleStyle = {
    fontSize: '16px',
    fontWeight: 'bold',
    marginTop: '15px',
};

const listStyle = {
    listStyleType: 'none',
    paddingLeft: 0,
};

const subListStyle = {
    listStyleType: 'none',
    paddingLeft: '20px',
};

const linkStyle = {
    color: 'black', // 글자색 검정
    textDecoration: 'none', // 밑줄 제거
    display: 'block',
    padding: '5px 0',
};

export default Sidebar;
