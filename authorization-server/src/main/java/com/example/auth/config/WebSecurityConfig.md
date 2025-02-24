

OpenID Connect 使用一个特殊的权限范围值 openid 来控制对 UserInfo 端点的访问。
OpenID Connect 定义了一组标准化的 OAuth 权限范围，对应于用户属性的子集profile、email、 phone、address，参见表格：


权限范围                     声明
openid                      sub
profile            Name、family_name、given_name、middle_name、nickname、preferred_username、profile、 picture、website、gender、birthdate、zoneinfo、locale、updated_at
email                   email、email_verified
address                 address,是一个 JSON 对象、包含 formatted、street_address、locality、region、postal_code、country
phone                   phone_number、phone_number_verified

