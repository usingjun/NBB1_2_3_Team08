module.exports = {
    plugins: [
        require('autoprefixer'),
        require('postcss-preset-env')({
            // 추가 설정을 원한다면 여기 추가
        })
    ]
};
