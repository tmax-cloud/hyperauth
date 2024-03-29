<#import "template.ftl" as layout>
<@layout.mainLayout active='sessions' bodyClass='sessions'; section>

    <@layout.contentHeader ; section>
        ${msg("sessionsHtmlTitle")}
    </@layout.contentHeader>

   <div id = 'page-descript'> ${msg("sessionHtmlBody")}</div>
        <hr id='hr-top'>

    <form action="${url.sessionsUrl}" method="post">
        <input type="hidden" id="stateChecker" name="stateChecker" value="${stateChecker}">
        <button id="logout-all-sessions" class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!}">${msg("doLogOutAllSessions")}</button>
    </form>

    <table class="table">
        <thead>
        <tr>
            <th >${msg("services")}</th>
            <th>${msg("ip")}</th>
            <th>${msg("started")}</th>
            <th class="tbl-hidden">${msg("lastAccess")}</th>
            <th class="tbl-hidden">${msg("expires")}</th>            
        </tr>
        </thead>

        <tbody>
        <#list sessions.sessions as session>
            <#list session.clients as client>
                <tr>
                    <td>${client}</td>
                    <td>${session.ipAddress}</td>
                    <td>${session.started?datetime}</td>
                    <td class="tbl-hidden">${session.lastAccess?datetime}</td>
                    <td class="tbl-hidden">${session.expires?datetime}</td>                
                </tr>
            </#list>
        </#list>
        </tbody>

    </table>

    

</@layout.mainLayout>
