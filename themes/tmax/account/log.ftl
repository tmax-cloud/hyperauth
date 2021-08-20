<#import "template.ftl" as layout>
<@layout.mainLayout active='log' bodyClass='log'; section>

    <@layout.contentHeader ; section>
        ${msg("accountLogHtmlTitle")}
    </@layout.contentHeader>

    <table class="table">
        <thead>
        <tr>
            <th>${msg("date")}</th>
            <th>${msg("event")}</th>
            <th>${msg("ip")}</th>
            <th>${msg("client")}</th>
            <th>${msg("details")}</th>
        </tr>
        </thead>

        <tbody>
        <#list log.events as event>
            <tr>
                <td>${event.date?datetime}</td>
                <td>${event.event}</td>
                <td>${event.ipAddress}</td>
                <td>${event.client!}</td>
                <td><#list event.details as detail>${detail.key} = ${detail.value} <#if detail_has_next>, </#if></#list></td>
            </tr>
        </#list>
        </tbody>

    </table>

</@layout.mainLayout>